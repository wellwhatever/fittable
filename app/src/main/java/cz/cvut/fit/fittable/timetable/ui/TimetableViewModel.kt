package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.core.util.todayDate
import cz.cvut.fit.fittable.shared.search.domain.GetFilteredDayEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.CachePersonalEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetCachedEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetPersonalDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetTimetableCalendarBoundsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.timetable.navigation.TimetableArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class TimetableViewModel(
    private val generateHoursGrid: GenerateHoursGridUseCase,
    private val getDayEvents: GetPersonalDayEventsGridUseCase,
    private val getFilteredDayEvents: GetFilteredDayEventsUseCase,
    private val cachePersonalEvents: CachePersonalEventsUseCase,
    private val getCachedEvents: GetCachedEventsUseCase,
    getCalendarBounds: GetTimetableCalendarBoundsUseCase
) : ViewModel() {
    private val searchResult = MutableStateFlow<TimetableArgs?>(null)
    private val hours = MutableStateFlow<List<TimetableHour>?>(null)
    private val error = MutableStateFlow<ApiException?>(null)
    private val selectedDate = MutableStateFlow(todayDate())
    private val events = MutableStateFlow<List<TimetableItem>>(emptyList())

    private val calendarBounds = flowOf(getCalendarBounds())

    private val headerState = combine(calendarBounds, selectedDate) { bounds, selectedDate ->
        HeaderState(
            calendarBounds = bounds,
            selectedDate = selectedDate
        )
    }

    val uiState: StateFlow<TimetableUiState> =
        combine(
            events,
            hours,
            error,
            headerState,
        ) { events, hours, error, header ->
            when {
                error != null -> error.mapTimetableException()
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
        combineEvents()
        cacheEvents()
    }

    private fun cacheEvents() {
        viewModelScope.launch {
            try {
                cachePersonalEvents()
            } catch (exception: ApiException) {
                // no-op if cache is failed
            }
        }
    }

    private fun combineEvents() {
        viewModelScope.launch {
            combine(selectedDate, searchResult) { date, search ->
                events.value = getEvents(date, search)
            }.collect()
        }
    }

    private suspend fun getEvents(
        date: LocalDate,
        search: TimetableArgs?
    ): List<TimetableItem> {
        return try {
            if (search == null) {
                getDayEvents(date)
            } else {
                getFilteredDayEvents(
                    day = date,
                    type = search.searchResult.eventCategory,
                    id = search.searchResult.eventId
                )
            }
        } catch (exception: ApiException) {
            when (exception) {
                !is HttpExceptionDomain -> getCachedEvents(date)
                else -> {
                    error.value = exception
                    emptyList()
                }
            }
        }
    }

    private fun fetchHoursGrid() {
        viewModelScope.launch {
            hours.value = generateHoursGrid()
        }
    }

    fun onReloadClick() {
        error.value = null
    }

    fun onContinueClick() {
        error.value = null
        searchResult.value = null
    }

    fun onDayClick(day: LocalDate) {
        viewModelScope.launch {
            selectedDate.value = day
        }
    }

    internal fun setSearchResult(args: TimetableArgs) {
        searchResult.value = args
    }
}
