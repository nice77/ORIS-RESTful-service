package com.example.task.data.remote.datasource

import com.example.task.data.remote.datasource.requests.RegisterRequest
import com.example.task.data.remote.datasource.requests.UserUpdateRequest
import com.example.task.data.remote.datasource.responses.AuthenticationResponse
import com.example.task.data.remote.datasource.responses.EventResponse
import com.example.task.data.remote.datasource.responses.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @POST("users/new")
    suspend fun register(@Body request : RegisterRequest) : AuthenticationResponse

    @GET("users")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getUsers(
        @Query("page") page : Int
    ) : List<UserResponse>

    @GET("users")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getUsersByName(
        @Query("page") page : Int,
        @Query("name") name : String
    ) : List<UserResponse>

    @GET("users/{id}/subscribed-events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getUsersSubscribedEvents(
        @Path("id") userId : Long,
        @Query("page") page : Int
    ) : List<EventResponse>

    @GET("users/{id}/created-events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getUsersCreatedEvents(
        @Path("id") userId : Long,
        @Query("page") page : Int
    ) : List<EventResponse>

    @GET("users/current-user")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getCurrentUserId() : Long

    @GET("users/{id}")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getUser(@Path("id") userId : Long) : UserResponse

    @PATCH("users")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun updateUser(@Body userUpdateRequest : UserUpdateRequest) : Boolean

    @POST("users/user-image")
    @Headers(StaticStrings.AUTH_HEADER)
    @Multipart
    suspend fun updateUserImage(@Part userImage : MultipartBody.Part) : String

    @POST("users/event")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun manageSubscriptionToEvent(@Query("event_id") eventId : Long)

    @GET("users/event")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun amISubscribedToEvent(@Query("event_id") eventId : Long) : Boolean

    @POST("users/{id}/subscribe")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun manageSubscriptionToUser(@Path("id") userId : Long)

    @GET("users/{id}/subscription")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun amISubscribedToUser(@Path("id") userId: Long) : Boolean
}