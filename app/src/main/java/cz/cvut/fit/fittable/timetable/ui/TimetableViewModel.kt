package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.core.remote.NoInternetException
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
    private val showNoInternetSnackBar = MutableStateFlow(false)

    private val calendarBounds = flowOf(getCalendarBounds())

    private val headerState =
        combine(calendarBounds, selectedDate, searchResult) { bounds, selectedDate, filters ->
            HeaderState(
                calendarBounds = bounds,
                selectedDate = selectedDate,
                hasActiveFilter = filters != null
            )
        }

    val uiState: StateFlow<TimetableUiState> =
        combine(
            events,
            hours,
            error,
            headerState,
            showNoInternetSnackBar
        ) { events, hours, error, header, showSnackBar ->
            when {
                error != null -> error.mapTimetableException()
                hours.isNullOrEmpty() || events.isEmpty() -> TimetableUiState.Loading
                else -> TimetableUiState.Content(
                    hoursGrid = hours,
                    events = events,
                    header = header,
                    showNoConnectionSnackBar = showSnackBar
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
            }.also {
                dismissNoInternetSnackBar()
            }
        } catch (exception: ApiException) {
            when (exception) {
                is NoInternetException -> {
                    showNoInternetSnackBar()
                    getCachedEvents(date)
                }

                else -> {
                    error.value = exception
                    emptyList()
                }
            }
        }
    }

    private fun showNoInternetSnackBar() {
        showNoInternetSnackBar.value = true
    }

    private fun dismissNoInternetSnackBar() {
        showNoInternetSnackBar.value = false
    }

    private fun fetchHoursGrid() {
        viewModelScope.launch {
            hours.value = generateHoursGrid()
        }
    }

    fun onReloadClick() {
        viewModelScope.launch {
            error.value = null
            events.value = getEvents(selectedDate.value, searchResult.value)
        }
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

    fun onFilterRemoveClick() {
        searchResult.value = null
    }

    internal fun setSearchResult(args: TimetableArgs) {
        searchResult.value = args
    }
}
