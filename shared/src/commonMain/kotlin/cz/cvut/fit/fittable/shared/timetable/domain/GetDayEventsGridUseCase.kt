package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours

class GetDayEventsGridUseCase(
    private val getUserEventsUseCase: GetUserEventsUseCase
) {
    suspend operator fun invoke(): List<TimetableItem> =
        createTimetableWithSpacers(getUserEventsUseCase())

    private fun createTimetableWithSpacers(events: List<TimetableEvent>): List<TimetableItem> {
        val now: Instant = Clock.System.now()
        val zone = TimeZone.currentSystemDefault()
        val currentDay = now.toLocalDateTime(zone).date.atStartOfDayIn(zone)
        val sortedEvents = events.sortedBy { it.start }

        var currentTime = currentDay + schoolDayStart
        val endTime = currentDay + schoolDayEnd

        val eventsIterator = sortedEvents.iterator()

        val timetable = mutableListOf<TimetableItem>()

        while (currentTime <= endTime) {
            if (eventsIterator.hasNext()) {
                val nextEvent = eventsIterator.next()
                val eventStart = nextEvent.start
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

    companion object {
        private val schoolDayStart = 7.hours
        private val schoolDayEnd = 21.hours
    }
}