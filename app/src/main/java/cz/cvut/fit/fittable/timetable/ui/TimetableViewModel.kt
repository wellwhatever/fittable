package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.HttpException
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimetableViewModel(
    private val generateHoursGrid: GenerateHoursGridUseCase,
    private val getDayEvents: GetDayEventsGridUseCase
) : ViewModel() {
    private val hours = MutableStateFlow<List<TimetableHour>?>(null)

    private val _selectedDate = MutableStateFlow(
        Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date.let { today ->
            HeaderState(
                today,
                today
            )
        }
    )

    private val events: Flow<List<TimetableItem>> = _selectedDate.map {
        getDayEvents(it.selectedDate)
    }

    private val error = MutableStateFlow<String?>(null)

    val uiState = combine(events, hours, error, _selectedDate) { events, hours, error, header ->
        when {
            error != null -> TimetableUiState.Error(error)
            hours.isNullOrEmpty() || events.isEmpty() -> TimetableUiState.Loading
            else -> TimetableUiState.Content(hoursGrid = hours, events = events, header = header)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TimetableUiState.Loading,
    )

    init {
        fetchHoursGrid()
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
            } catch (exception: HttpException) {
                error.value = exception.message
            }
        }
    }

    private fun fetchHoursGrid() {
        viewModelScope.launch {
            try {
                hours.value = generateHoursGrid()
            } catch (exception: HttpException) {
                error.value = exception.message
            }
        }
    }

    fun onReloadClick() {
        // TODO handle reload click
    }

    fun onEventClick(event: TimetableEvent) {
        // TODO handle event click
    }

    fun onDayClick(day: LocalDate) {
        viewModelScope.launch {
            _selectedDate.update { it.copy(selectedDate = day) }
        }
    }
}
