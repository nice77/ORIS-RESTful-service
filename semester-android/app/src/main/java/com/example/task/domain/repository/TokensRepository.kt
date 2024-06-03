package com.example.task.domain.repository

import com.example.task.domain.models.TokensDomainModel

interface TokensRepository {
    fun addTokens(tokens : TokensDomainModel)

    fun removeTokens()

    fun getTokens() : TokensDomainModel?

    fun updateTokens(tokens : TokensDomainModel)
}