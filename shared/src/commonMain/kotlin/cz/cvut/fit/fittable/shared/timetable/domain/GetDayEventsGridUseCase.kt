package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.MergedEvents
import cz.cvut.fit.fittable.shared.timetable.domain.model.RequestDateBounds
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflictContent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEventContainer
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class GetDayEventsGridUseCase internal constructor(
    private val getUserEventsUseCase: GetUserEventsUseCase,
    private val eventConflictCalculator: EventConflictCalculator,
    private val eventsConverterDomain: EventsConverterDomain,
) {
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = getRequestDataBounds(day)
        val events = getUserEventsUseCase(from = startDate, to = endDate)

        // We have to merge conflicted events as they're considered as single element
        val mergedEvents = eventConflictCalculator.groupOverlappingEvents(events)
        val mergedEventsWithSeparators = composeTimetableItems(mergedEvents)

        return createTimetableWithSpacers(events = mergedEventsWithSeparators, day = day)
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

    private fun composeTimetableItems(events: List<MergedEvents>): List<TimetableEventContainer> {
        return events.map { event ->
            val conflicts = event.events
            val conflictsWithSpacers = conflicts.map { conflict ->
                val startDifference = event.start - conflict.start
                val endDifference = event.end - conflict.end
                val startSpacer =
                    TimetableSpacer(startDifference).takeIf { startDifference != Duration.ZERO }
                val endSpacer =
                    TimetableSpacer(endDifference).takeIf { endDifference != Duration.ZERO }

                TimetableConflictContent(
                    spacerStart = startSpacer,
                    spacerEnd = endSpacer,
                    event = eventsConverterDomain.toTimetableItem(conflict)
                )
            }
            TimetableEventContainer(
                events = conflictsWithSpacers, start = event.start, end = event.end
            )
        }
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
                    timetable.add(TimetableSpacer(eventStart - currentTime))
                }
                timetable.add(nextEvent)
                currentTime = eventStart + nextEvent.duration
            } else {
                timetable.add(TimetableSpacer(endTime - currentTime))
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