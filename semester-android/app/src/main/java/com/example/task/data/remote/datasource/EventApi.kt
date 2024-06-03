package com.example.task.data.remote.datasource

import com.example.task.data.remote.datasource.requests.EventCreateRequest
import com.example.task.data.remote.datasource.requests.EventUpdateRequest
import com.example.task.data.remote.datasource.responses.EventResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("events/{id}")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getEvent(@Path("id") id : Long) : EventResponse

    @GET("events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getEvents(@Query("page") page : Int) : List<EventResponse>

    @GET("events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getEventsByName(@Query("name") name : String, @Query("page") page : Int) : List<EventResponse>

    @PATCH("events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun updateEvent(@Body eventUpdateRequest: EventUpdateRequest)

    @POST("events")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun addEvent(@Body eventCreateRequest: EventCreateRequest) : EventResponse

    @POST("events/{id}/event-image")
    @Headers(StaticStrings.AUTH_HEADER)
    @Multipart
    suspend fun addImage(@Path("id") eventId : Long, @Part image : MultipartBody.Part)

    @HTTP(method = "DELETE", path = "events/{id}/event-image", hasBody = true)
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun deleteImages(@Path("id") eventId: Long, @Body imageNameList : List<String>)
}