package com.example.task.data.repositories

import com.example.task.data.mapper.ToDomainModelMapper
import com.example.task.data.remote.datasource.CommentApi
import com.example.task.data.remote.datasource.requests.CommentRequest
import com.example.task.domain.models.CommentDomainModel
import com.example.task.domain.models.request.CommentRequestDomainModel
import com.example.task.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi,
    private val toDomainModelMapper: ToDomainModelMapper
) : CommentRepository{

    override suspend fun getComments(eventId: Long, page: Int): List<CommentDomainModel> {
        return commentApi.getComments(eventId, page).map(toDomainModelMapper::mapCommentResponseToCommentDomainModel)
    }

    override suspend fun addComment(
        eventId: Long,
        commentRequestDomainModel: CommentRequestDomainModel
    ) : CommentDomainModel {
        return toDomainModelMapper.mapCommentResponseToCommentDomainModel(
            commentApi.addComment(
                eventId = eventId,
                CommentRequest(text = commentRequestDomainModel.text, date = commentRequestDomainModel.date)
            )
        )
    }

}