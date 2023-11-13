package cz.cvut.fit.fittable.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.timetable.navigation.EventDetailArgs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EventDetailViewModel(
    private val getEventById: GetEventByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val topicArgs: EventDetailArgs = EventDetailArgs(savedStateHandle)

    private val _eventDetail: Flow<EventDetail?> = flow {
        emit(null)
        emit(getEventById(topicArgs.eventId))
    }

    val uiState: StateFlow<EventDetailState> = _eventDetail.map { event ->
        when (event) {
            null -> EventDetailState.Loading
            else -> EventDetailState.Content(event)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EventDetailState.Loading,
    )
}


sealed interface EventDetailState {
    data class Content(val eventDetail: EventDetail) : EventDetailState
    data class Error(val message: String) : EventDetailState
    data object Loading : EventDetailState
}