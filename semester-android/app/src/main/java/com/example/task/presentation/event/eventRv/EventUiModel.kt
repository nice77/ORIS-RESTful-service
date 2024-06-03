package com.example.task.presentation.event.eventRv

import com.example.task.domain.models.UserDomainModel
import java.util.Date

sealed interface EventUiModel {

    data class Event(
        val id : Long,
        val date : Date,
        val title : String,
        val description : String,
        val latitude : Double,
        val longitude : Double,
        val authorId : Long,
        val eventImages : List<String>
    ) : EventUiModel

    data object CommentInputField : EventUiModel

    data class Comment(
        val id : Long,
        val text : String,
        val date : Date,
        val userId : Long,
        val userName : String,
        val userAvatar : String
    ) : EventUiModel
}