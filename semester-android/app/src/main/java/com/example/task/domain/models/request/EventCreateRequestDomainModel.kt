package com.example.task.domain.models.request

import java.util.Date

data class EventCreateRequestDomainModel(
    var date: Date,
    var name: String,
    var description: String,
    var latitude: Double,
    var longitude: Double
)