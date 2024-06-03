package com.example.task.presentation.main.mainRv

import androidx.annotation.StringRes

sealed interface MainUiModel {
    data class Event(
        val id : Long,
        val title : String,
        val eventImage : String
    ): MainUiModel
    data class Title(@StringRes val textRes: Int): MainUiModel
    data object Users: MainUiModel
}
