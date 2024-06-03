package com.example.task.domain.usecases

import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(id : Long) : Result<EventDomainModel> {
        return runSuspendCatching {
            eventRepository.getEvent(id)
        }
    }

}