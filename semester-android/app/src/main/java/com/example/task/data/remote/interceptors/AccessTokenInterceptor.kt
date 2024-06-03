package com.example.task.data.remote.interceptors

import com.example.task.data.local.sharedpreferences.TokensPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val tokensPreferences: TokensPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.headers.contains(Pair("Authorization", "true"))) {
            val tokens = tokensPreferences.getTokens()
            tokens?.let {
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer ${it.access}")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(request)
    }
}