package com.example.task.data.repositories

import com.example.task.data.mapper.ToDomainModelMapper
import com.example.task.data.remote.datasource.EventApi
import com.example.task.data.remote.datasource.StaticStrings
import com.example.task.data.remote.datasource.requests.EventCreateRequest
import com.example.task.data.remote.datasource.requests.EventUpdateRequest
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.request.EventCreateRequestDomainModel
import com.example.task.domain.models.request.EventUpdateRequestDomainModel
import com.example.task.domain.repository.EventRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val toDomainModelMapper: ToDomainModelMapper,
    private val eventApi: EventApi
) : EventRepository {
    override suspend fun getEvent(id: Long): EventDomainModel {
        return toDomainModelMapper.mapEventResponseToEventDomainModel(eventApi.getEvent(id = id))
    }

    override suspend fun getEvents(page: Int): List<EventDomainModel> {
        return eventApi.getEvents(page = page).map(toDomainModelMapper::mapEventResponseToEventDomainModel)
    }

    override suspend fun getEventsByTitle(page: Int, title: String): List<EventDomainModel> {
        return eventApi.getEventsByName(
            name = title,
            page = page
        ).map(toDomainModelMapper::mapEventResponseToEventDomainModel)
    }

    override suspend fun updateEvent(eventUpdateRequestDomainModel: EventUpdateRequestDomainModel) {
        eventUpdateRequestDomainModel.run {
            eventApi.updateEvent(eventUpdateRequest = EventUpdateRequest(
                id = id,
                date = date,
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            ))
        }
    }

    override suspend fun addEvent(eventCreateRequestDomainModel: EventCreateRequestDomainModel) : EventDomainModel{
        return toDomainModelMapper.mapEventResponseToEventDomainModel(eventCreateRequestDomainModel.run {
            eventApi.addEvent(eventCreateRequest = EventCreateRequest(
                date = date,
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            ))
        })
    }

    override suspend fun addImages(eventId : Long, files: List<File>) {
        files.forEach {
            val requestFile = it.asRequestBody(if (it.name.endsWith(".png")) StaticStrings.IMAGE_PNG_CONTENT_TYPE else StaticStrings.IMAGE_JPG_CONTENT_TYPE)
            eventApi.addImage(eventId = eventId, MultipartBody.Part.createFormData("file", it.name, requestFile))
        }
    }

    override suspend fun deleteImages(eventId : Long, imageNames: List<String>) {
        eventApi.deleteImages(eventId = eventId, imageNameList = imageNames)
    }
}