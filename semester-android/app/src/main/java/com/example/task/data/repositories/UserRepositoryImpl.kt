package com.example.task.data.repositories

import com.example.task.data.remote.datasource.UserApi
import com.example.task.data.remote.datasource.requests.RegisterRequest
import com.example.task.data.mapper.ToDomainModelMapper
import com.example.task.data.remote.datasource.StaticStrings
import com.example.task.data.remote.datasource.requests.UserUpdateRequest
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.TokensDomainModel
import com.example.task.domain.models.UserDomainModel
import com.example.task.domain.models.UserUpdateDomainModel
import com.example.task.domain.models.request.RegisterRequestDomainModel
import com.example.task.domain.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val toDomainModelMapper: ToDomainModelMapper
) : UserRepository {
    override suspend fun register(request: RegisterRequestDomainModel) : TokensDomainModel {
        return toDomainModelMapper.mapAuthResponseToTokensDomainModel(
            userApi.register(
                RegisterRequest(
                    name = request.name,
                    email = request.email,
                    password = request.password)
            )
        )
    }

    override suspend fun getUsers(page : Int) : List<UserDomainModel> {
        return userApi.getUsers(
            page = page
        ).map { userResponse ->
            toDomainModelMapper.mapUserResponseToUserDomainModel(userResponse)
        }
    }

    override suspend fun getUsersByName(page: Int, name: String): List<UserDomainModel> {
        return userApi.getUsersByName(
            page = page,
            name = name
        ).map { userResponse ->
            toDomainModelMapper.mapUserResponseToUserDomainModel(userResponse)
        }
    }

    override suspend fun getUsersSubscribedEvents(userId: Long?, page : Int): List<EventDomainModel> {
        return userApi.getUsersSubscribedEvents(
            userId = userId ?: getCurrentUserId(),
            page = page
        ).map(toDomainModelMapper::mapEventResponseToEventDomainModel)
    }

    override suspend fun getUsersCreatedEvents(userId: Long?, page: Int): List<EventDomainModel> {
        return userApi.getUsersCreatedEvents(
            userId = userId ?: getCurrentUserId(),
            page = page
        ).map(toDomainModelMapper::mapEventResponseToEventDomainModel)
    }

    override suspend fun getCurrentUserId(): Long {
        return userApi.getCurrentUserId()
    }

    override suspend fun getUser(userId: Long?): UserDomainModel {
        return toDomainModelMapper.mapUserResponseToUserDomainModel(userApi.getUser(userId ?: getCurrentUserId()))
    }

    override suspend fun updateUser(userUpdateDomainModel: UserUpdateDomainModel) {
        val userToUpdateId = getUser(null)
        userApi.updateUser(UserUpdateRequest(
            id = userToUpdateId.id,
            name = userUpdateDomainModel.name,
            age = userUpdateDomainModel.age,
            email = userToUpdateId.email,
            city = userUpdateDomainModel.city
        ))
    }

    override suspend fun updateUserImage(file: File): String {
        val requestFile = file.asRequestBody(StaticStrings.IMAGE_PNG_CONTENT_TYPE)
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return userApi.updateUserImage(filePart)
    }

    override suspend fun manageSubscriptionToEvent(eventId: Long) {
        userApi.manageSubscriptionToEvent(eventId)
    }

    override suspend fun amISubscribedToEvent(eventId: Long): Boolean {
        return userApi.amISubscribedToEvent(eventId)
    }

    override suspend fun manageSubscriptionToUser(userId: Long) {
        userApi.manageSubscriptionToUser(userId = userId)
    }

    override suspend fun amISubscribedToUser(userId: Long): Boolean {
        return userApi.amISubscribedToUser(userId = userId)
    }
}