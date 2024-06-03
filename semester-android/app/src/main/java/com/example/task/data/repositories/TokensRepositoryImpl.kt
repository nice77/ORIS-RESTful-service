package com.example.task.data.repositories

import com.example.task.data.local.sharedpreferences.TokensEntity
import com.example.task.data.local.sharedpreferences.TokensPreferences
import com.example.task.domain.models.TokensDomainModel
import com.example.task.domain.repository.TokensRepository
import javax.inject.Inject

class TokensRepositoryImpl @Inject constructor(
    private val preferences: TokensPreferences
) : TokensRepository {

    override fun addTokens(tokens : TokensDomainModel) {
        preferences.addTokens(TokensEntity(access = tokens.access, refresh = tokens.refresh))
    }

    override fun removeTokens() {
        preferences.removeTokens()
    }

    override fun getTokens() : TokensDomainModel? {
        val tokens = preferences.getTokens()
        tokens?.let {
            return TokensDomainModel(access = it.access, refresh = it.refresh)
        }
        return null
    }

    override fun updateTokens(tokens : TokensDomainModel) {
        preferences.updateTokens(TokensEntity(access = tokens.access, refresh = tokens.refresh))
    }

}