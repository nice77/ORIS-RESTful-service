package com.example.task.domain.repository

import com.example.task.domain.models.TokensDomainModel
import com.example.task.domain.models.request.AuthenticationRequestDomainModel
import com.example.task.domain.models.request.RefreshRequestDomainModel

interface AuthRepository {
    suspend fun authenticate(request: AuthenticationRequestDomainModel) : TokensDomainModel

    suspend fun refreshToken(request: RefreshRequestDomainModel) : TokensDomainModel
}