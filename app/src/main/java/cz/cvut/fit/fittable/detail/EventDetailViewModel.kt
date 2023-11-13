package cz.cvut.fit.fittable.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.domain.GetUserEventsUseCase
import cz.cvut.fit.fittable.timetable.navigation.EventDetailArgs
import kotlinx.coroutines.flow.MutableStateFlow

class EventDetailViewModel(
    private val getEventById: GetUserEventsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val topicArgs: EventDetailArgs = EventDetailArgs(savedStateHandle)

    private val _eventDetail = MutableStateFlow<EventDetail?>(null)


}

internal fun