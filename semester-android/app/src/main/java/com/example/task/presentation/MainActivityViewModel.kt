package com.example.task.presentation

import androidx.lifecycle.ViewModel
import com.example.task.domain.usecases.CheckIfAuthenticatedUseCase
import com.example.task.domain.usecases.DeleteCurrentDestinationUseCase
import com.example.task.domain.usecases.GetCurrentDestinationUseCase
import com.example.task.domain.usecases.GetFirstRunUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainActivityViewModel @AssistedInject constructor(
    private val getFirstRunUseCase: GetFirstRunUseCase,
    private val checkIfAuthenticatedUseCase: CheckIfAuthenticatedUseCase,
    private val getCurrentDestinationUseCase: GetCurrentDestinationUseCase,
    private val deleteCurrentDestinationUseCase: DeleteCurrentDestinationUseCase
) :  ViewModel() {

    fun getFirstRun() = getFirstRunUseCase()

    fun checkIfAuthenticated() = checkIfAuthenticatedUseCase()

    fun getCurrentDestination() = getCurrentDestinationUseCase()

    fun deleteCurrentDestination() = deleteCurrentDestinationUseCase()

    @AssistedFactory
    interface Factory {
        fun create() : MainActivityViewModel
    }
}