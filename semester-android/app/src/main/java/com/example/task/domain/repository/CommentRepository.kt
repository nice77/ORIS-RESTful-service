package com.example.task.domain.repository

import com.example.task.domain.models.CommentDomainModel
import com.example.task.domain.models.request.CommentRequestDomainModel

interface CommentRepository {

    suspend fun getComments(eventId : Long, page : Int) : List<CommentDomainModel>

    suspend fun addComment(eventId: Long, commentRequestDomainModel: CommentRequestDomainModel) : CommentDomainModel
}