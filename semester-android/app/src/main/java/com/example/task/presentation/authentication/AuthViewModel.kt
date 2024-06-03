package com.example.task.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.domain.models.errorEnum.AuthErrorEnum
import com.example.task.domain.models.request.AuthenticationRequestDomainModel
import com.example.task.domain.usecases.AuthenticateUserUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class AuthViewModel @AssistedInject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<AuthErrorEnum>()
    val errorFlow : SharedFlow<AuthErrorEnum>
        get() = _errorFlow

    private val _submitFlow = MutableSharedFlow<Boolean>()
    val submitFlow : SharedFlow<Boolean>
        get() = _submitFlow

    @AssistedFactory
    interface Factory {
        fun create() : AuthViewModel
    }

    fun authenticateUser(email : String, password: String) {
        val request = AuthenticationRequestDomainModel (
            email = email,
            password = password
        )
        viewModelScope.launch {
            val result = authenticateUserUseCase(request)
            result.onFailure { exception ->
                when (exception) {
                    is HttpException -> _errorFlow.emit(AuthErrorEnum.WRONG_CREDENTIALS)
                    is UnknownHostException -> _errorFlow.emit(AuthErrorEnum.UNKNOWN_HOST)
                }
            }.onSuccess {
                _submitFlow.emit(true)
            }
        }
    }

}