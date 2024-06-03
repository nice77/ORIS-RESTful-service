package com.example.task.domain.validators

import android.util.Patterns
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
){
    operator fun invoke(email : String) : Boolean {
        return email.matches(Regex(Patterns.EMAIL_ADDRESS.pattern()))
    }
}