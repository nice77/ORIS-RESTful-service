package com.example.task.domain.usecases

import com.example.task.domain.repository.CurrentDestinationRepository
import javax.inject.Inject

class SetCurrentDestinationUseCase @Inject constructor(
    private val currentDestinationRepository: CurrentDestinationRepository
) {

    operator fun invoke(resId : Int) = currentDestinationRepository.setCurrentDestination(resId)

}