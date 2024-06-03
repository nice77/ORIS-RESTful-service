package com.example.task.data.remote.datasource.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id : Long,
    @SerialName("name")
    val name : String,
    @SerialName("age")
    val age : Int,
    @SerialName("email")
    val email : String,
    @SerialName("city")
    val city : String,
    @SerialName("userImage")
    val userImage : String,
    @SerialName("authorsCount")
    val authorsCount : Int,
    @SerialName("subscribersCount")
    val subscribersCount : Int
)
