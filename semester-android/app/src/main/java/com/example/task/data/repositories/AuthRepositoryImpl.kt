package com.example.task.data.repositories

import com.example.task.data.remote.datasource.AuthApi
import com.example.task.data.remote.datasource.requests.AuthenticationRequest
import com.example.task.data.remote.datasource.requests.RefreshRequest
import com.example.task.data.mapper.ToDomainModelMapper
import com.example.task.domain.models.request.AuthenticationRequestDomainModel
import com.example.task.domain.models.TokensDomainModel
import com.example.task.domain.models.request.RefreshRequestDomainModel
import com.example.task.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val toDomainModelMapper: ToDomainModelMapper
) : AuthRepository {

    override suspend fun authenticate(request: AuthenticationRequestDomainModel) : TokensDomainModel {
        return toDomainModelMapper.mapAuthResponseToTokensDomainModel(
            authApi.authenticate(AuthenticationRequest(email = request.email, password = request.password))
        )
    }

    override suspend fun refreshToken(request: RefreshRequestDomainModel) : TokensDomainModel {
        return toDomainModelMapper.mapAuthResponseToTokensDomainModel(
            authApi.refreshToken(RefreshRequest(refresh = request.refresh))
        )
    }

}