package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours

class GetDayEventsGridUseCase(
    private val getUserEventsUseCase: GetUserEventsUseCase
) {
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = getRequestDataBounds(day)
        val events = getUserEventsUseCase(from = startDate, to = endDate)
        return createTimetableWithSpacers(events = events, day = day)
    }

    private fun getRequestDataBounds(day: LocalDate): RequestDateBounds {
        val defaultTimeZone = TimeZone.currentSystemDefault()
        val startOfDayInstant = day
            .atTime(0, 0, 0)
            .toInstant(defaultTimeZone)
        val endOfDayInstant = startOfDayInstant + 24.hours

        val startDate = startOfDayInstant.toLocalDateTime(defaultTimeZone).date
        val endDate = endOfDayInstant.toLocalDateTime(defaultTimeZone).date
        return RequestDateBounds(startDate, endDate)
    }

    private fun createTimetableWithSpacers(
        events: List<TimetableEvent>,
        day: LocalDate
    ): List<TimetableItem> {
        val startOfCurrentDay = day.atStartOfDayIn(TimeZone.currentSystemDefault())
        val sortedEvents = events.sortedBy { it.start }

        var currentTime = startOfCurrentDay + schoolDayStart
        val endTime = startOfCurrentDay + schoolDayEnd

        val eventsIterator = sortedEvents.iterator()

        val timetable = mutableListOf<TimetableItem>()

        while (currentTime <= endTime) {
            if (eventsIterator.hasNext()) {
                val nextEvent = eventsIterator.next()
                val eventStart = nextEvent.start
                if (currentTime < eventStart) {
                    timetable.add(
                        TimetableSpacer(
                            duration = eventStart - currentTime,
                            id = "spacer_${timetable.lastIndex + 1}"
                        )
                    )
                }
                timetable.add(nextEvent)
                currentTime = eventStart + nextEvent.duration
            } else {
                timetable.add(
                    TimetableSpacer(
                        duration = endTime - currentTime,
                        id = "spacer_${timetable.lastIndex + 1}"
                    )
                )
                break
            }
        }

        return timetable
    }

    private data class RequestDateBounds(
        val from: LocalDate,
        val to: LocalDate
    )

    companion object {
        private val schoolDayStart = 7.hours
        private val schoolDayEnd = 21.hours
    }
}