package com.example.task.domain.usecases

import com.example.task.domain.repository.FirstRunRepository
import javax.inject.Inject

class GetFirstRunUseCase @Inject constructor(
    private val firstRunRepository: FirstRunRepository
) {

    operator fun invoke() = firstRunRepository.getFirstRun()

}