package com.example.task.data.remote.datasource.responses

import com.example.task.data.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CommentResponse(
    @SerialName("id")
    val id : Long,
    @SerialName("text")
    val text : String,
    @SerialName("date")
    @Serializable(with = DateSerializer::class)
    val date : Date,
    @SerialName("userDto")
    val userResponse: UserResponse
)
