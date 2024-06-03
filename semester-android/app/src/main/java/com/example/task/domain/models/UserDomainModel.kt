package com.example.task.domain.models


data class UserDomainModel(
    val id : Long,
    val name : String,
    val age : Int,
    val email : String,
    val city : String,
    val userImage : String,
    val authorsCount : Int,
    val subscribersCount : Int
)
