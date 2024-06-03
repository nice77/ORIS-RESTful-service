package com.example.task.presentation.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.models.UserUpdateDomainModel
import com.example.task.domain.usecases.GetUserUseCase
import com.example.task.domain.usecases.UpdateUserImageUseCase
import com.example.task.domain.usecases.UpdateUserUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream

class EditProfileViewModel @AssistedInject constructor(
    private val getUserUseCase : GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val updateUserImageUseCase : UpdateUserImageUseCase
) : ViewModel() {

    private val _userFlow = MutableStateFlow<UserDomainModel?>(null)
    val userFlow : StateFlow<UserDomainModel?>
        get() = _userFlow

    private val _submitFlow = MutableStateFlow(false)
    val submitFlow : StateFlow<Boolean>
        get() = _submitFlow

    fun getUserById() {
        viewModelScope.launch {
            getUserUseCase(userId = null).onSuccess {result ->
                _userFlow.emit(result)
            }
        }
    }

    fun updateUserImage(inputStream: InputStream?) {
        viewModelScope.launch {
            updateUserImageUseCase(inputStream)
        }
    }

    fun onSubmitButtonClicked(name : String, age : Int, city : String) {
        val userUpdateDomainModel = UserUpdateDomainModel(
            name = name,
            age = age,
            city = city
        )
        viewModelScope.launch {
            updateUserUseCase(userUpdateDomainModel = userUpdateDomainModel).onSuccess {
                _submitFlow.emit(true)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create() : EditProfileViewModel
    }

}