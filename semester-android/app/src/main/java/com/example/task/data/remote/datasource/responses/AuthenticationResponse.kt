package com.example.task.data.remote.datasource.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    @SerialName("access")
    val access : String,
    @SerialName("refresh")
    val refresh : String
)
