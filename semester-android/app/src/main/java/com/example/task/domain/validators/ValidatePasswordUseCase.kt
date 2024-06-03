package com.example.task.domain.validators

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(
){
    operator fun invoke(password : String) : Boolean {
        return password.matches(regex)
    }

    companion object {
        val regex = Regex("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*]).{8,}\$")
    }
}