package com.example.task.domain.usecases

import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class DeleteEventImagesUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(eventId : Long, imageNameList : List<String>) : Result<Unit> {
        return runSuspendCatching {
            eventRepository.deleteImages(eventId = eventId, imageNames = imageNameList)
        }
    }

}