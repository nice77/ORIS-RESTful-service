package com.example.task.data.remote.datasource

import com.example.task.data.remote.datasource.requests.AuthenticationRequest
import com.example.task.data.remote.datasource.requests.RefreshRequest
import com.example.task.data.remote.datasource.responses.AuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/authorization")
    suspend fun authenticate(@Body request : AuthenticationRequest) : AuthenticationResponse

    @POST("auth/refresh_token")
    suspend fun refreshToken(@Body request : RefreshRequest) : AuthenticationResponse
}