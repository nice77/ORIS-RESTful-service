package com.example.task.domain.usecases

import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(page : Int) : Result<List<EventDomainModel>> {
        return runSuspendCatching {
            eventRepository.getEvents(page = page)
        }
    }

}