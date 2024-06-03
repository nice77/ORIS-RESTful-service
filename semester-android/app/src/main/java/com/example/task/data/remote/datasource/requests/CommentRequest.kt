package com.example.task.data.remote.datasource.requests

import com.example.task.data.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CommentRequest(
    @SerialName("text")
    val text : String,
    @SerialName("date")
    @Serializable(with = DateSerializer::class)
    val date : Date
)