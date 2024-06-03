package com.example.task.presentation.themechoose

import androidx.lifecycle.ViewModel
import com.example.task.domain.usecases.SetCurrentDestinationUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ThemeChooseViewModel @AssistedInject constructor(
    private val setCurrentDestinationUseCase: SetCurrentDestinationUseCase
) : ViewModel() {

    fun setCurrentDestination(resId : Int) = setCurrentDestinationUseCase(resId)

    @AssistedFactory
    interface Factory {
        fun create() : ThemeChooseViewModel
    }
}