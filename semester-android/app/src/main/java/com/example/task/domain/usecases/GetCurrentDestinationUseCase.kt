package com.example.task.domain.usecases

import com.example.task.domain.repository.CurrentDestinationRepository
import javax.inject.Inject

class GetCurrentDestinationUseCase @Inject constructor(
    private val currentDestinationRepository: CurrentDestinationRepository
){

    operator fun invoke() = currentDestinationRepository.getCurrentDestination()

}