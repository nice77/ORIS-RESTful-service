package com.example.task.domain.usecases

import com.example.task.domain.repository.UserRepository
import com.example.task.utils.runSuspendCatching
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(inputStream : InputStream?) : Result<String> {
        return runSuspendCatching {
            val bytes = inputStream?.readBytes()
            if (bytes?.isNotEmpty() == true) {
                val file = File.createTempFile("image", ".jpg")
                file.writeBytes(bytes)
                inputStream.close()
                return@runSuspendCatching userRepository.updateUserImage(file)
            }
            throw NoSuchElementException()
        }
    }

}