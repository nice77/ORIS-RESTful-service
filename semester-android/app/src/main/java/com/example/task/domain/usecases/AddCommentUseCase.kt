package com.example.task.domain.usecases

import com.example.task.domain.models.CommentDomainModel
import com.example.task.domain.models.request.CommentRequestDomainModel
import com.example.task.domain.repository.CommentRepository
import com.example.task.utils.runSuspendCatching
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
){

    suspend operator fun invoke(eventId : Long, commentRequestDomainModel: CommentRequestDomainModel) : Result<CommentDomainModel> {
        return runSuspendCatching {
            commentRepository.addComment(eventId = eventId, commentRequestDomainModel = commentRequestDomainModel)
        }
    }

}