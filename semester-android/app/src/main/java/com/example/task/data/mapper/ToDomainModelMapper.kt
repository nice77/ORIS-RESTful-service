package com.example.task.data.mapper

import com.example.task.data.remote.datasource.responses.AuthenticationResponse
import com.example.task.data.remote.datasource.responses.CommentResponse
import com.example.task.data.remote.datasource.responses.EventResponse
import com.example.task.data.remote.datasource.responses.UserResponse
import com.example.task.domain.models.CommentDomainModel
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.TokensDomainModel
import com.example.task.domain.models.UserDomainModel
import javax.inject.Inject

class ToDomainModelMapper @Inject constructor(
){

    fun mapAuthResponseToTokensDomainModel(response : AuthenticationResponse) : TokensDomainModel {
        return TokensDomainModel(
            access = response.access,
            refresh = response.refresh
        )
    }

    fun mapUserResponseToUserDomainModel(response : UserResponse) : UserDomainModel {
        return UserDomainModel(
            id = response.id,
            name = response.name,
            age = response.age,
            email = response.email,
            city = response.city,
            userImage = response.userImage,
            authorsCount = response.authorsCount,
            subscribersCount = response.subscribersCount
        )
    }

    fun mapEventResponseToEventDomainModel(response: EventResponse) : EventDomainModel {
        return response.run {
            EventDomainModel(
                id = id,
                date = date,
                title = title,
                description = description,
                latitude = latitude,
                longitude = longitude,
                authorId = authorId,
                eventImages = eventImages
            )
        }
    }

    fun mapCommentResponseToCommentDomainModel(response: CommentResponse) : CommentDomainModel {
        return response.run {
            CommentDomainModel(
                id = id,
                text = text,
                date = date,
                userDomainModel = mapUserResponseToUserDomainModel(userResponse)
            )
        }
    }
}