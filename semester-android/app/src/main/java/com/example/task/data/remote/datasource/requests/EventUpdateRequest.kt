package com.example.task.data.remote.datasource.requests

import com.example.task.data.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class EventUpdateRequest(
    @SerialName("id")
    var id: Long,
    @SerialName("date")
    @Serializable(with = DateSerializer::class)
    var date: Date,
    @SerialName("name")
    var name: String,
    @SerialName("description")
    var description: String,
    @SerialName("latitude")
    var latitude: Double,
    @SerialName("longitude")
    var longitude: Double
)
