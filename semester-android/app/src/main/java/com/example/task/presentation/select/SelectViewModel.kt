package com.example.task.presentation.select

import androidx.lifecycle.ViewModel
import com.example.task.domain.usecases.SetFirstRunUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SelectViewModel @AssistedInject constructor(
    private val setFirstRunUseCase: SetFirstRunUseCase
) : ViewModel() {

    fun setFirstRun() = setFirstRunUseCase()

    @AssistedFactory
    interface Factory {
        fun create() : SelectViewModel
    }
}