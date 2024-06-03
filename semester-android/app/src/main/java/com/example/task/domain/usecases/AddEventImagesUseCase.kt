package com.example.task.domain.usecases

import android.content.ContentResolver
import android.net.Uri
import com.example.task.domain.repository.EventRepository
import com.example.task.utils.runSuspendCatching
import java.io.File
import javax.inject.Inject

class AddEventImagesUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(eventId : Long, contentResolver: ContentResolver, uriList : List<Uri>) : Result<Unit> {
        return runSuspendCatching {
            val fileList = uriList.map { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                if (bytes?.isNotEmpty() == true) {
                    val file = File.createTempFile("file", ".jpg")
                    file.writeBytes(bytes)
                    inputStream.close()
                    return@map file
                }
                throw NoSuchElementException()
            }
            eventRepository.addImages(eventId = eventId, files = fileList)
        }
    }

}