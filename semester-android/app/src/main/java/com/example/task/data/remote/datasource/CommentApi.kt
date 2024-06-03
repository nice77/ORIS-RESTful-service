package com.example.task.data.remote.datasource

import com.example.task.data.remote.datasource.requests.CommentRequest
import com.example.task.data.remote.datasource.responses.CommentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {
    @GET("events/{id}/comments")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun getComments(@Path("id") eventId : Long, @Query("page") page : Int) : List<CommentResponse>

    @POST("events/{id}/comments")
    @Headers(StaticStrings.AUTH_HEADER)
    suspend fun addComment(@Path("id") eventId: Long, @Body commentRequest: CommentRequest) : CommentResponse
}