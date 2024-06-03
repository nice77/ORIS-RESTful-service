package com.example.task.presentation.editevent

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.domain.models.EventDomainModel
import com.example.task.domain.models.request.EventCreateRequestDomainModel
import com.example.task.domain.models.request.EventUpdateRequestDomainModel
import com.example.task.domain.usecases.AddEventImagesUseCase
import com.example.task.domain.usecases.AddEventUseCase
import com.example.task.domain.usecases.DeleteEventImagesUseCase
import com.example.task.domain.usecases.GetEventUseCase
import com.example.task.domain.usecases.UpdateEventUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class EditEventViewModel @AssistedInject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val deleteEventImagesUseCase: DeleteEventImagesUseCase,
    private val addEventImagesUseCase: AddEventImagesUseCase,
    @Assisted private val eventId : Long?
) : ViewModel() {

    private val _eventFlow = MutableStateFlow<EventDomainModel?>(null)
    val eventFlow : StateFlow<EventDomainModel?>
        get() = _eventFlow

    private val _submitFlow = MutableStateFlow(Pair(false, false))
    val submitFlow : StateFlow<Pair<Boolean, Boolean>>
        get() = _submitFlow

    private val _coordsFlow = MutableStateFlow<Pair<Double, Double>?>(null)
    val coordsFlow : StateFlow<Pair<Double, Double>?>
        get() = _coordsFlow


    fun getEvent() {
        viewModelScope.launch {
            eventId?.let {
                getEventUseCase(id = it).onSuccess {
                    _eventFlow.emit(it)
                }
            }
        }
    }

    fun onSubmitButtonClicked(
        title : String,
        description : String,
        latitude : Double,
        longitude : Double,
        imageNameList: List<String>,
        uriList : List<Uri>,
        contentResolver: ContentResolver,
        selectedDate : Date
    ) {
        viewModelScope.launch {
            if (eventId == null) {
                addEventUseCase(eventCreateRequestDomainModel = EventCreateRequestDomainModel(
                    date = selectedDate,
                    name = title,
                    description = description,
                    latitude = latitude,
                    longitude = longitude
                )).onSuccess {
                    addEventImagesUseCase(eventId = it.id, contentResolver = contentResolver, uriList = uriList).onSuccess {
                        _submitFlow.emit(Pair(true, true))
                    }
                }
            } else {
                updateEventUseCase(eventUpdateRequestDomainModel = EventUpdateRequestDomainModel(
                    date = selectedDate,
                    name = title,
                    description = description,
                    latitude = latitude,
                    longitude = longitude,
                    id = eventId
                )).onSuccess {
                    _submitFlow.emit(Pair(false, false))
                    addEventImagesUseCase(eventId = eventId, contentResolver = contentResolver, uriList = uriList).onSuccess {
                        _submitFlow.emit(submitFlow.value.copy(first = true))
                    }
                    deleteEventImagesUseCase(eventId = eventId, imageNameList = imageNameList).onSuccess {
                        _submitFlow.emit(submitFlow.value.copy(second = true))
                    }
                }
            }
        }
    }

    fun submitCoords(pair: Pair<Double, Double>) {
        viewModelScope.launch {
            _coordsFlow.emit(pair)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(eventId: Long?) : EditEventViewModel
    }

}