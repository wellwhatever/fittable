package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.timetable.domain.model.TimetableGridHour
import cz.cvut.fit.fittable.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.timetable.domain.model.TimetableSpacer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class TimetableViewModel(
    private val generateHoursGrid: GenerateHoursGridUseCase
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
//            events.value = timetableRepository.getEvents()

            val now: Instant = Clock.System.now()
            val event1start = now - 10.hours
            val event1end = event1start + 1.hours + 30.minutes
            val duration1 = event1end - event1start

            val event2start = event1end
            val event2end = event2start + 1.hours + 30.minutes
            val event2duration = event2end - event2start

            val res = listOf(
                TimetableEvent(
                    title = "Test1",
                    start = event1start.toLocalDateTime(TimeZone.currentSystemDefault()),
                    duration = duration1
                ),
                TimetableEvent(
                    title = "Test2",
                    start = event2start.toLocalDateTime(TimeZone.currentSystemDefault()),
                    duration = event2duration
                )
            )


            val timetable = createTimetableWithSpacers(res)

            events.value = timetable
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

fun createTimetableWithSpacers(events: List<TimetableEvent>): List<TimetableItem> {
    val now: Instant = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()

    val sortedEvents = events.sortedBy { it.start }

    val firstEvent = sortedEvents.firstOrNull()

    firstEvent ?: return emptyList()
    var currentTime = firstEvent.start.date.atStartOfDayIn(zone) + 7.hours
    val endTime = firstEvent.start.date.atStartOfDayIn(zone) + 21.hours

    val eventsIterator = sortedEvents.iterator()

    val timetable = mutableListOf<TimetableItem>()

    while (currentTime <= endTime) {
        if (eventsIterator.hasNext()) {
            val nextEvent = eventsIterator.next()
            val eventStart = nextEvent.start.toInstant(zone)
            if (currentTime < eventStart) {
                timetable.add(TimetableSpacer(duration = eventStart - currentTime))
            }
            timetable.add(nextEvent)
            currentTime = eventStart + nextEvent.duration
        } else {
            timetable.add(TimetableSpacer(duration = endTime - currentTime))
            break
        }
    }

    return timetable
}

sealed interface TimetableUiState

data class TimetableContent(
    val hoursGrid: List<TimetableGridHour>,
    val events: List<TimetableItem>
) : TimetableUiState

data object TimetableLoading : TimetableUiState
