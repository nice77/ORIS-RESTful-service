package com.example.task.domain.usecases

import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.repository.UserRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        page : Int
    ) : Result<List<UserDomainModel>> {
        return runSuspendCatching {
            userRepository.getUsers(page)
        }
    }

}