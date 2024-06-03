package com.example.task.data.remote.interceptors

import com.example.task.data.local.sharedpreferences.TokensEntity
import com.example.task.data.local.sharedpreferences.TokensPreferences
import com.example.task.data.remote.datasource.AuthApi
import com.example.task.data.remote.datasource.requests.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    private val tokensPreferences: TokensPreferences,
    private val authApi: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 403) {
            val tokens = tokensPreferences.getTokens()
            tokens?.let {
                val refreshRequest = RefreshRequest(tokens.refresh)
                val authenticationResponseResult = runBlocking {
                    runCatching {
                        authApi.refreshToken(refreshRequest)
                    }.getOrNull()
                }
                authenticationResponseResult?.let {
                    val tokensEntity = TokensEntity(
                        access = it.access,
                        refresh = it.refresh
                    )
                    tokensPreferences.updateTokens(tokensEntity)
                    response.close()
                    return chain.proceed(
                        Request.Builder()
                            .url(chain.request().url)
                            .method(chain.request().method, chain.request().body)
                            .addHeader("Authorization", "Bearer ${tokensEntity.access}")
                            .build()
                    )
                }
            }
        }
        return response
    }
}