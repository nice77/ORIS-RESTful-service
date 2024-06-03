package com.example.task.data.mapper

import com.example.task.data.local.sharedpreferences.TokensEntity
import com.example.task.data.remote.datasource.responses.AuthenticationResponse
import javax.inject.Inject

class ToSharedPreferencesMapper @Inject constructor(
) {

    fun mapToTokensEntity(response: AuthenticationResponse) : TokensEntity {
        return TokensEntity(
            access = response.access,
            refresh = response.refresh
        )
    }

}