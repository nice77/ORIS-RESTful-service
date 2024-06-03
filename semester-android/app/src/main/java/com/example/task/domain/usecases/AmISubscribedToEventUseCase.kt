package com.example.task.domain.usecases

import com.example.task.domain.repository.UserRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class AmISubscribedToEventUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(eventId : Long) : Result<Boolean> {
        return runSuspendCatching {
            userRepository.amISubscribedToEvent(eventId = eventId)
        }
    }

}