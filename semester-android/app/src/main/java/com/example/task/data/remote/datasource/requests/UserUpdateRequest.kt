package com.example.task.data.remote.datasource.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest (
    @SerialName("id")
    val id : Long,
    @SerialName("name")
    val name : String,
    @SerialName("age")
    val age : Int,
    @SerialName("email")
    val email : String,
    @SerialName("city")
    val city : String
)