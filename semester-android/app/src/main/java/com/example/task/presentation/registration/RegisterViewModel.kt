package com.example.task.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.domain.models.errorEnum.RegisterErrorEnum
import com.example.task.domain.models.request.RegisterRequestDomainModel
import com.example.task.domain.usecases.RegisterUserUseCase
import com.example.task.domain.validators.ValidateEmailUseCase
import com.example.task.domain.validators.ValidatePasswordUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class RegisterViewModel @AssistedInject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<RegisterErrorEnum>()
    val errorFlow : SharedFlow<RegisterErrorEnum>
        get() = _errorFlow

    private val _submitFlow = MutableSharedFlow<Boolean>()
    val submitFlow : SharedFlow<Boolean>
        get() = _submitFlow

    @AssistedFactory
    interface Factory {
        fun create() : RegisterViewModel
    }

    fun registerUser(name : String, email : String, password : String) {
        viewModelScope.launch {
            val request = RegisterRequestDomainModel(
                name = name,
                email = email,
                password = password
            )
            val result = registerUserUseCase(request)
            result.onFailure { error ->
                when (error) {
                    is HttpException -> _errorFlow.emit(RegisterErrorEnum.EMAIL_IN_USE)
                    is UnknownHostException -> _errorFlow.emit(RegisterErrorEnum.UNKNOWN_HOST)
                }
            }.onSuccess {
                _submitFlow.emit(true)
            }
        }
    }

    fun validateEmail(text : String) : Boolean {
        return validateEmailUseCase(text)
    }

    fun validatePassword(text : String) : Boolean {
        return validatePasswordUseCase(text)
    }
}