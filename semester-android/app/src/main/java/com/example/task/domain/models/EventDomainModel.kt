package com.example.task.domain.models

import java.util.Date

data class EventDomainModel(
    val id : Long,
    val date : Date,
    val title : String,
    val description : String,
    val latitude : Double,
    val longitude : Double,
    val authorId : Long,
    val eventImages : List<String>
)
