package com.example.task.presentation.profile.profileRv

sealed interface ProfileUIModel {

    data class User (
        val id : Long,
        val name : String,
        val age : Int,
        val email : String,
        val city : String,
        val userImage : String,
        val subscribersCount : Int,
        val authorsCount : Int,
        val isCurrentUser : Boolean,
        val isSubscribed : Boolean?
    ) : ProfileUIModel

    data object Buttons : ProfileUIModel

    data class Event(
        val id : Long,
        val title : String,
        val eventImage : String
    ) : ProfileUIModel
}