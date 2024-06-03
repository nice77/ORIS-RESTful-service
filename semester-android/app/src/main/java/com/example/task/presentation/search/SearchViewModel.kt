package com.example.task.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.task.R
import com.example.task.data.repositories.paging.EventByNamePagingSource
import com.example.task.data.repositories.paging.UserByNamePagingSource
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.UserDomainModel
import com.example.task.presentation.search.searchRv.SearchUiModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel @AssistedInject constructor(
    private val eventByNamePagingSourceFactory: EventByNamePagingSource.Factory,
    private val userByNamePagingSourceFactory: UserByNamePagingSource.Factory
): ViewModel() {

    private val _configFlow = MutableStateFlow(
        SearchConfig(
            checkedId = R.id.option_event_rb,
            query = null
        )
    )
    val configFlow : StateFlow<SearchConfig>
        get() = _configFlow

    val listItems : StateFlow<PagingData<SearchUiModel>> = _configFlow
        .flatMapLatest { config ->
            val pager = when (config.checkedId) {
                R.id.option_event_rb -> createNewEventPager(config.query ?: "")
                R.id.option_users_rb -> createNewUsersPager(config.query ?: "")
                else -> throw NoSuchMethodException()
            }
            pager
                .flow
                .map { pagingData ->
                    pagingData.map {
                        when (it) {
                            is EventDomainModel -> {
                                SearchUiModel.Event(
                                    id = it.id,
                                    title = it.title,
                                    eventImage = if (it.eventImages.isEmpty()) "" else it.eventImages[0]
                                )
                            }
                            is UserDomainModel -> {
                                SearchUiModel.User(
                                    id = it.id,
                                    name = it.name,
                                    userImage = it.userImage
                                )
                            }
                            else -> NoSuchElementException()
                        }
                    } as PagingData<SearchUiModel>
                }
                .map {
                    it.insertHeaderItem(item = SearchUiModel.SearchBar(query = config.query ?: ""))
                }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @AssistedFactory
    interface Factory {
        fun create() : SearchViewModel
    }

    fun setupNewSearchConfig(config : SearchConfig) {
        viewModelScope.launch {
            _configFlow.emit(config)
        }
    }

    private fun createNewEventPager(query : String) : Pager<Int, EventDomainModel> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            )
        ) {
            eventByNamePagingSourceFactory.create(query)
        }
    }

    private fun createNewUsersPager(query : String) : Pager<Int, UserDomainModel> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            )
        ) {
            userByNamePagingSourceFactory.create(query)
        }
    }
}