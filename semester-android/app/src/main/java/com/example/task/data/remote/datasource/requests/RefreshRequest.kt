package com.example.task.data.remote.datasource.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerialName("refresh")
    private val refresh : String
)
