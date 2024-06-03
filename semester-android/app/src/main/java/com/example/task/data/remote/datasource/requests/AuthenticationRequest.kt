package com.example.task.data.remote.datasource.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(
    @SerialName("email")
    val email : String,
    @SerialName("password")
    val password : String
)
