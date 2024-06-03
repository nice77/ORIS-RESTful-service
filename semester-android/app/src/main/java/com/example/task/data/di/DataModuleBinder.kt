package com.example.task.data.di

import com.example.task.data.di.qualifiers.AccessTokenInterceptorQualifier
import com.example.task.data.di.qualifiers.RefreshTokenInterceptorQualifier
import com.example.task.data.remote.interceptors.AccessTokenInterceptor
import com.example.task.data.remote.interceptors.RefreshTokenInterceptor
import com.example.task.data.repositories.AuthRepositoryImpl
import com.example.task.data.repositories.CurrentDestinationRepositoryImpl
import com.example.task.data.repositories.CommentRepositoryImpl
import com.example.task.data.repositories.EventRepositoryImpl
import com.example.task.data.repositories.FirstRunRepositoryImpl
import com.example.task.data.repositories.TokensRepositoryImpl
import com.example.task.data.repositories.UserRepositoryImpl
import com.example.task.domain.repository.AuthRepository
import com.example.task.domain.repository.CommentRepository
import com.example.task.domain.repository.CurrentDestinationRepository
import com.example.task.domain.repository.EventRepository
import com.example.task.domain.repository.FirstRunRepository
import com.example.task.domain.repository.TokensRepository
import com.example.task.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor

@Module
interface DataModuleBinder {

    @Binds
    @AccessTokenInterceptorQualifier
    fun bindAccessTokenInterceptorToInterface(accessTokenInterceptor: AccessTokenInterceptor) : Interceptor

    @Binds
    @RefreshTokenInterceptorQualifier
    fun bindRefreshTokenInterceptorToInterface(refreshTokenInterceptor: RefreshTokenInterceptor) : Interceptor

    @Binds
    fun bindAuthRepositoryImplToInterface(authRepositoryImpl: AuthRepositoryImpl) : AuthRepository

    @Binds
    fun bindTokensRepositoryImplToInterface(tokensRepositoryImpl: TokensRepositoryImpl) : TokensRepository

    @Binds
    fun bindUserRepositoryImplToInterface(userRepositoryImpl: UserRepositoryImpl) : UserRepository

    @Binds
    fun bindEventRepositoryImplToInterface(eventRepositoryImpl: EventRepositoryImpl) : EventRepository

    @Binds
    fun bindFirstRunRepositoryImplToInterface(firstRunRepositoryImpl: FirstRunRepositoryImpl) : FirstRunRepository

    @Binds
    fun bindCurrentDestinationRepoImplToInterface(currentDestinationRepositoryImpl: CurrentDestinationRepositoryImpl) : CurrentDestinationRepository

    @Binds
    fun bindCommentRepositoryImplToInterface(commentRepositoryImpl: CommentRepositoryImpl) : CommentRepository
}