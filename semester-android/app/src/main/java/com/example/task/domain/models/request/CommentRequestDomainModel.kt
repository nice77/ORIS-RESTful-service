package com.example.task.domain.models.request

import java.util.Date

data class CommentRequestDomainModel(
    val text : String,
    val date : Date
)