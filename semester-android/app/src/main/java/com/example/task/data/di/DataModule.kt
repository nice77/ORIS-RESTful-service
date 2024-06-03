package com.example.task.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.task.BuildConfig
import com.example.task.data.di.qualifiers.AccessTokenInterceptorQualifier
import com.example.task.data.di.qualifiers.AuthRetrofitQualifier
import com.example.task.data.di.qualifiers.NonAuthRetrofitQualifier
import com.example.task.data.di.qualifiers.RefreshTokenInterceptorQualifier
import com.example.task.data.local.sharedpreferences.Keys
import com.example.task.data.remote.datasource.AuthApi
import com.example.task.data.remote.datasource.CommentApi
import com.example.task.data.remote.datasource.EventApi
import com.example.task.data.remote.datasource.StaticStrings
import com.example.task.data.remote.datasource.UserApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
class DataModule {

    @Provides
    fun provideUnsafeOkClientBuilder(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            val trustAllCerts: Array<TrustManager> = arrayOf(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(loggingInterceptor)
            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }

    @Provides
    fun provideOkHttpClient(
        @AccessTokenInterceptorQualifier accessTokenInterceptor: Interceptor,
        @RefreshTokenInterceptorQualifier refreshTokenInterceptorQualifier: Interceptor,
        builder: OkHttpClient.Builder
    ) : OkHttpClient {
        return builder
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(refreshTokenInterceptorQualifier)
            .build()
    }

    @Provides
    @Singleton
    @NonAuthRetrofitQualifier
    fun provideNonAuthRetrofit(
        builder : OkHttpClient.Builder
    ) : Retrofit {
        return Retrofit.Builder()
            .client(builder.build())
            .baseUrl(BuildConfig.PATH)
            .addConverterFactory(Json.asConverterFactory(StaticStrings.CONTENT_TYPE))
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofitQualifier
    fun provideAuthRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PATH)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(StaticStrings.CONTENT_TYPE))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @NonAuthRetrofitQualifier retrofit: Retrofit
    ) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(
        @AuthRetrofitQualifier retrofit: Retrofit
    ) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventApi(
        @AuthRetrofitQualifier retrofit: Retrofit
    ) : EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApi(
        @AuthRetrofitQualifier retrofit: Retrofit
    ) : CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    @Singleton
    fun providePreferences(ctx : Context) : SharedPreferences {
        return ctx.getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE)
    }
}