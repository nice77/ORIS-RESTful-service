package com.example.task.domain.usecases

import com.example.task.domain.models.request.AuthenticationRequestDomainModel
import com.example.task.domain.repository.AuthRepository
import com.example.task.domain.repository.TokensRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val tokensRepository: TokensRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        request: AuthenticationRequestDomainModel,
    ) : Result<Unit> {
        return runSuspendCatching {
            val tokens = authRepository.authenticate(request)
            tokensRepository.addTokens(tokens)
        }
    }
}