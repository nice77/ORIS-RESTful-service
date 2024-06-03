package com.example.task.domain.usecases

import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.repository.UserRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId : Long?) : Result<UserDomainModel> {
        return runSuspendCatching {
            userRepository.getUser(userId = userId)
        }
    }

}