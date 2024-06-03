package com.example.task.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.task.R
import com.example.task.data.repositories.paging.EventPagingSource
import com.example.task.data.repositories.paging.UserPagingSource
import com.example.task.presentation.main.mainRv.MainUiModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel @AssistedInject constructor(
    private val eventPagingSourceFactory: EventPagingSource.Factory,
    private val userPagingSourceFactory: UserPagingSource.Factory
) : ViewModel() {

    var eventPagingSource : EventPagingSource? = null
    var userPagingSource : UserPagingSource? = null

    val eventsFlow = Pager(
        PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
            prefetchDistance = 1
        )
    ) {
        eventPagingSource = eventPagingSourceFactory.create()
        eventPagingSource!!
    }.flow
        .map { pagingData ->
            pagingData.map {
                MainUiModel.Event(
                    id = it.id,
                    title = it.title,
                    eventImage = if (it.eventImages.isEmpty()) "" else it.eventImages[0]
                )
            } as PagingData<MainUiModel>
        }
        .map {
            it.insertHeaderItem(item = MainUiModel.Title(R.string.recommended_events))
                .insertHeaderItem(item = MainUiModel.Users)
                .insertHeaderItem(item = MainUiModel.Title(R.string.recommended_users))
        }
        .cachedIn(viewModelScope)
        .stateIn(scope = viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val usersFlow = Pager(
        PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
            prefetchDistance = 1
        )
    ) {
        userPagingSource = userPagingSourceFactory.create()
        userPagingSource!!
    }.flow
        .cachedIn(viewModelScope)
        .stateIn(scope = viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun reloadData() {
        userPagingSource?.invalidate()
        eventPagingSource?.invalidate()
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainViewModel
    }
}
