package com.example.task.domain.usecases

import com.example.task.domain.repository.TokensRepository
import javax.inject.Inject

class CheckIfAuthenticatedUseCase @Inject constructor(
    private val tokensRepository: TokensRepository
) {

    operator fun invoke() = tokensRepository.getTokens() != null

}