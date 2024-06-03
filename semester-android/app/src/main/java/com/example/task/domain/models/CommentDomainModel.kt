package com.example.task.domain.models

import java.util.Date

data class CommentDomainModel(
    val id : Long,
    val text : String,
    val date : Date,
    val userDomainModel: UserDomainModel
)
