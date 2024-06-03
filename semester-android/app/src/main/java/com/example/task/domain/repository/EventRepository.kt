package com.example.task.domain.repository

import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.request.EventCreateRequestDomainModel
import com.example.task.domain.models.request.EventUpdateRequestDomainModel
import java.io.File

interface EventRepository {

    suspend fun getEvent(id : Long) : EventDomainModel

    suspend fun getEvents(page : Int) : List<EventDomainModel>

    suspend fun getEventsByTitle(page : Int, title : String) : List<EventDomainModel>

    suspend fun updateEvent(eventUpdateRequestDomainModel: EventUpdateRequestDomainModel)

    suspend fun addEvent(eventCreateRequestDomainModel: EventCreateRequestDomainModel) : EventDomainModel

    suspend fun addImages(eventId : Long, files : List<File>)

    suspend fun deleteImages(eventId : Long, imageNames : List<String>)
}