package com.example.task.presentation.editevent.bottomsheet

sealed interface BottomUiModel {

    data object Button : BottomUiModel

    data class Image(
        val path : String,
        val isUri : Boolean
    ) : BottomUiModel

}