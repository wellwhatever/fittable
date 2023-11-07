package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableGridHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimetableViewModel(
    private val generateHoursGrid: GenerateHoursGridUseCase,
    private val getDayEvents: GetDayEventsGridUseCase
) : ViewModel() {
    private val hours = MutableStateFlow<List<TimetableGridHour>?>(null)
    private val events = MutableStateFlow<List<TimetableItem>?>(null)

    val uiState = combine(events, hours) { events, hours ->
        if (hours.isNullOrEmpty() || events.isNullOrEmpty()) {
            TimetableLoading
        } else {
            TimetableContent(hoursGrid = hours, events = events)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )

    init {
        fetchHoursGrid()
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            events.value = getDayEvents()
        }
    }

    private fun fetchHoursGrid() {
        viewModelScope.launch {
            hours.value = generateHoursGrid()
        }
    }

    fun onEventClick(event: TimetableEvent) {
        // TODO handle event click
    }
}
