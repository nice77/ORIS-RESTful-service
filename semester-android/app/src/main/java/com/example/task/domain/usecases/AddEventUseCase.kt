package com.example.task.domain.usecases

import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.request.EventCreateRequestDomainModel
import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(eventCreateRequestDomainModel: EventCreateRequestDomainModel) : Result<EventDomainModel> {
        return runSuspendCatching {
            eventRepository.addEvent(eventCreateRequestDomainModel = eventCreateRequestDomainModel)
        }
    }

}