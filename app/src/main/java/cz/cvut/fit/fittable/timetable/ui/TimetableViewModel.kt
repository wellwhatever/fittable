package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.search.domain.GetFilteredDayEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetTimetableHeaderUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.timetable.navigation.TimetableArgs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class TimetableViewModel(
    private val generateHoursGrid: GenerateHoursGridUseCase,
    private val getDayEvents: GetDayEventsGridUseCase,
    private val getFilteredDayEvents: GetFilteredDayEventsUseCase,
    getTimetableHeader: GetTimetableHeaderUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
//    private val searchResult: Flow<TimetableArgs?> =
//        savedStateHandle.getStateFlow<String?>(
//            key = TIMETABLE_SEARCH_RESULT_ARG, initialValue = null
//        ).mapLatest { raw ->
//            Log.e("handle:", "value:${savedStateHandle.get<String>(TIMETABLE_SEARCH_RESULT_ARG)}")
//            if (raw.isNullOrBlank().not()) {
//                TimetableArgs(raw.orEmpty())
//            } else {
//                null
//            }
//        }

    private val searchResult = MutableStateFlow<TimetableArgs?>(null)
    private val hours = MutableStateFlow<List<TimetableHour>?>(null)

    private val _selectedDate = MutableStateFlow(
        with(getTimetableHeader()) {
            HeaderState(
                monthStart = monthStart,
                monthEnd = monthEnd,
                today = today,
                selectedDate = selectedDate
            )
        }
    )

    private val events: Flow<List<TimetableItem>> =
        combine(_selectedDate, searchResult) { date, search ->
            if (search == null) {
                getDayEvents(date.selectedDate)
            } else {
                getFilteredDayEvents(
                    type = search.searchResult.eventCategory,
                    id = search.searchResult.eventId,
                    day = date.selectedDate
                )
            }
        }

    private val error = MutableStateFlow<String?>(null)

    val uiState =
        combine(
            events,
            hours,
            error,
            _selectedDate,
        ) { events, hours, error, header ->
            when {
                error != null -> TimetableUiState.Error(error)
                hours.isNullOrEmpty() || events.isEmpty() -> TimetableUiState.Loading
                else -> TimetableUiState.Content(
                    hoursGrid = hours,
                    events = events,
                    header = header
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TimetableUiState.Loading,
        )

    init {
        fetchHoursGrid()
    }

    private fun fetchHoursGrid() {
        viewModelScope.launch {
            try {
                hours.value = generateHoursGrid()
            } catch (exception: HttpExceptionDomain) {
                error.value = exception.message
            }
        }
    }

    fun onReloadClick() {
        // TODO handle reload click
    }

    fun onDayClick(day: LocalDate) {
        viewModelScope.launch {
            _selectedDate.update { it.copy(selectedDate = day) }
        }
    }

    internal fun setSearchResult(args: TimetableArgs) {
        searchResult.value = args
    }
}
