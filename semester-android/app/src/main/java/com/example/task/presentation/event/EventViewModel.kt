package com.example.task.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.task.data.repositories.paging.CommentPagingSource
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.models.request.CommentRequestDomainModel
import com.example.task.domain.usecases.AddCommentUseCase
import com.example.task.domain.usecases.AmISubscribedToEventUseCase
import com.example.task.domain.usecases.GetEventUseCase
import com.example.task.domain.usecases.GetUserUseCase
import com.example.task.domain.usecases.ManageSubscriptionToEventUseCase
import com.example.task.presentation.event.eventRv.EventUiModel
import com.example.task.presentation.event.eventRv.RequestResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class EventViewModel @AssistedInject constructor(
    @Assisted private val eventId : Long,
    private val getEventUseCase: GetEventUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val commentPagingSourceFactory: CommentPagingSource.Factory,
    private val manageSubscriptionToEventUseCase: ManageSubscriptionToEventUseCase,
    private val amISubscribedToEventUseCase: AmISubscribedToEventUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModel() {

    private var _currentEvent : EventDomainModel? = null
    val currentEvent : EventDomainModel?
        get() = _currentEvent

    private val _userFlow = MutableStateFlow<UserDomainModel?>(null)
    val userFlow : StateFlow<UserDomainModel?>
        get() = _userFlow

    private val _requestCurrentUserFlow = MutableStateFlow<Boolean?>(null)
    val requestCurrentUserFlow : StateFlow<Boolean?>
        get() = _requestCurrentUserFlow

    private val _requestSubscriptionFlow = MutableStateFlow<Boolean?>(null)
    val requestSubscriptionFlow : StateFlow<Boolean?>
        get() = _requestSubscriptionFlow


    private val _commentFlow = MutableStateFlow<EventUiModel.Comment?>(null)
    val commentFlow : StateFlow<EventUiModel.Comment?>
        get() = _commentFlow

    private var currentPagingSource : CommentPagingSource? = null

    val eventPageFlow : StateFlow<PagingData<EventUiModel>> = Pager(
        PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 1
        )
    ) {
        val toReturn = commentPagingSourceFactory.create(eventId)
        currentPagingSource = toReturn
        toReturn
    }.flow
        .map { pagingData ->
            pagingData.map { commentDomainModel ->
                commentDomainModel.run {
                    EventUiModel.Comment(
                        id = id,
                        text = text,
                        date = date,
                        userId = userDomainModel.id,
                        userName = userDomainModel.name,
                        userAvatar = userDomainModel.userImage
                    )
                }
            } as PagingData<EventUiModel>
        }
        .map { pagingData ->
            var newPagingData : PagingData<EventUiModel>
            if (_currentEvent == null) {
                getEventUseCase(eventId).onSuccess { eventDomainModel ->
                    _currentEvent = eventDomainModel
                }
            }
            newPagingData = pagingData.insertHeaderItem(item = EventUiModel.CommentInputField)
            _currentEvent?.run {
                newPagingData = newPagingData.insertHeaderItem(item =
                    EventUiModel.Event(
                        id = id,
                        date = date,
                        title = title,
                        description = description,
                        latitude = latitude,
                        longitude = longitude,
                        authorId = authorId,
                        eventImages = eventImages
                    )
                )
            }
            newPagingData
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    fun getUser(userId : Long) {
        viewModelScope.launch {
            getUserUseCase(userId = userId).onSuccess {
                _userFlow.emit(it)
            }
        }
    }

    fun reloadEventData() {
        _currentEvent = null
        currentPagingSource?.invalidate()
    }

    fun getIsCurrentUser(userId: Long) {
        viewModelScope.launch {
            getUserUseCase(null).onSuccess {
                _requestCurrentUserFlow.emit(it.id == userId)
            }
        }
    }

    fun subscribeToEvent() {
        viewModelScope.launch {
            manageSubscriptionToEventUseCase(eventId = eventId)
        }
    }

    fun amISubscribedToEvent() {
        viewModelScope.launch {
            amISubscribedToEventUseCase(eventId = eventId).onSuccess {
                _requestSubscriptionFlow.emit(it)
            }
        }
    }

    fun sendComment(text : String) {
        viewModelScope.launch {
            addCommentUseCase(eventId, CommentRequestDomainModel(text = text, date = Date())).onSuccess {
                _commentFlow.emit(EventUiModel.Comment(
                    id = it.id,
                    text = it.text,
                    date = it.date,
                    userId = it.userDomainModel.id,
                    userName = it.userDomainModel.name,
                    userAvatar = it.userDomainModel.userImage
                ))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted eventId: Long) : EventViewModel
    }

}