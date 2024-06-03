package com.example.task.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.task.R
import com.example.task.data.repositories.paging.UserCreatedEventPagingSource
import com.example.task.data.repositories.paging.UserSubscribedEventPagingSource
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.usecases.AmISubscribedToUserUseCase
import com.example.task.domain.usecases.GetUserUseCase
import com.example.task.domain.usecases.ManageSubscriptionToUserUseCase
import com.example.task.presentation.profile.profileRv.ProfileUIModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel @AssistedInject constructor(
    private val userCreatedEventPagingSourceFactory: UserCreatedEventPagingSource.Factory,
    private val userSubscribedEventPagingSourceFactory: UserSubscribedEventPagingSource.Factory,
    private val getUserUseCase: GetUserUseCase,
    private val amISubscribedToUserUseCase: AmISubscribedToUserUseCase,
    private val manageSubscriptionToUserUseCase: ManageSubscriptionToUserUseCase,
    @Assisted private val userId : Long?
) : ViewModel() {

    private var currentUser : UserDomainModel? = null

    private val _profileConfigFlow = MutableStateFlow(R.id.created_events_rb)
    val profileConfigFlow : StateFlow<Int>
        get() = _profileConfigFlow

    private var currentSource : PagingSource<Int, EventDomainModel>? = null

    val eventList : StateFlow<PagingData<ProfileUIModel>> = _profileConfigFlow
        .flatMapLatest { checkedItem ->
            val pager = when (checkedItem) {
                R.id.created_events_rb -> createNewCreatedEventsPager()
                R.id.subscribed_events_rb -> createNewSubscribedEventsPager()
                else -> throw NoSuchMethodException()
            }
            pager
                .flow.map { pagingData ->
                    pagingData.map {
                        ProfileUIModel.Event(
                            id = it.id,
                            title = it.title,
                            eventImage = if (it.eventImages.isEmpty()) "" else it.eventImages[0]
                        )
                    } as PagingData<ProfileUIModel>
                }
                .map { pagingData ->
                    var newPagingData : PagingData<ProfileUIModel>
                    newPagingData = pagingData.insertHeaderItem(item = ProfileUIModel.Buttons)
                    if (currentUser == null) {
                        getUserUseCase(userId = userId).onSuccess {
                            currentUser = it
                        }
                    }
                    var loggedUser : UserDomainModel? = null
                    getUserUseCase(null).onSuccess {
                        loggedUser = it
                    }
                    var isSubscribed : Boolean? = null
                    currentUser?.let {
                        userId?.let {
                            amISubscribedToUserUseCase(it).onSuccess {
                                isSubscribed = it
                            }
                        }
                        newPagingData = newPagingData.insertHeaderItem(item = ProfileUIModel.User(
                            id = it.id,
                            name = it.name,
                            age = it.age,
                            email = it.email,
                            city = it.city,
                            userImage = it.userImage,
                            subscribersCount = it.subscribersCount,
                            authorsCount = it.authorsCount,
                            isCurrentUser = userId == null || loggedUser?.id == currentUser?.id,
                            isSubscribed = isSubscribed
                        ))
                    }
                    newPagingData
                }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private fun createNewCreatedEventsPager() : Pager<Int, EventDomainModel> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            )
        ) {
            val userCreatedEventPagingSource = userCreatedEventPagingSourceFactory.create(userId = userId)
            currentSource = userCreatedEventPagingSource
            userCreatedEventPagingSource
        }
    }

    private fun createNewSubscribedEventsPager() : Pager<Int, EventDomainModel> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            )
        ) {
            val userSubscribedEventPagingSource = userSubscribedEventPagingSourceFactory.create(userId = userId)
            currentSource = userSubscribedEventPagingSource
            userSubscribedEventPagingSource
        }
    }

    fun reloadProfileData() {
        currentUser = null
        currentSource?.invalidate()
    }

    fun checkNewItem(checkedItemId : Int) {
        viewModelScope.launch {
            _profileConfigFlow.emit(checkedItemId)
        }
    }

    fun manageSubscriptionToUser() {
        viewModelScope.launch {
            userId?.let {
                manageSubscriptionToUserUseCase(userId = userId)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted userId: Long?) : ProfileViewModel
    }
}