package com.example.task.domain.models.request

data class RegisterRequestDomainModel(
    val name : String,
    val email : String,
    val password : String
)
