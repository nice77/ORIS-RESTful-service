package com.example.task.domain.usecases

import com.example.task.domain.models.UserUpdateDomainModel
import com.example.task.domain.repository.UserRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userUpdateDomainModel: UserUpdateDomainModel) : Result<Unit> {
        return runSuspendCatching {
            userRepository.updateUser(userUpdateDomainModel = userUpdateDomainModel)
        }
    }

}