package com.example.task.domain.usecases

import com.example.task.domain.models.request.EventUpdateRequestDomainModel
import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(eventUpdateRequestDomainModel: EventUpdateRequestDomainModel) : Result<Unit> {
        return runSuspendCatching {
            eventRepository.updateEvent(eventUpdateRequestDomainModel = eventUpdateRequestDomainModel)
        }
    }

}