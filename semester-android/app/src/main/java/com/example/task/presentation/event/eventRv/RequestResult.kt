package com.example.task.presentation.event.eventRv

import com.example.task.presentation.event.RequestResultTypeEnum

data class RequestResult(
    val type : RequestResultTypeEnum,
    val result : Boolean
)