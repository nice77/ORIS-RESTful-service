package com.example.task.data.remote.datasource.responses

import com.example.task.data.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class EventResponse(
    @SerialName("id")
    val id : Long,
    @SerialName("date")
    @Serializable(with = DateSerializer::class)
    val date : Date,
    @SerialName("name")
    val title : String,
    @SerialName("description")
    val description : String,
    @SerialName("latitude")
    val latitude : Double,
    @SerialName("longitude")
    val longitude : Double,
    @SerialName("authorId")
    val authorId : Long,
    @SerialName("eventImages")
    val eventImages : List<String>
)