package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.util.DisjointSet
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflict
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflictItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
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

class GetDayEventsGridUseCase(
    private val getUserEventsUseCase: GetUserEventsUseCase
) {
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = getRequestDataBounds(day)
        val events = getUserEventsUseCase(from = startDate, to = endDate)

        // We have to merge conflicted events as they're considered as single element
        val mergedEvents = groupOverlappingEvents(events)
        val mergedEventsWithSeparators = addSeparatorsToMergedEvents(mergedEvents)

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

    private fun addSeparatorsToMergedEvents(events: List<TimetableEvent>): List<TimetableEvent> {
        return events.map { event ->
            if (event is TimetableConflict) {
                val conflicts = event.conflictedEvents
                val conflictsWithSpacers = conflicts.map { conflict ->
                    val startDifference = event.start - conflict.start
                    val endDifference = event.end - conflict.end
                    val startSpacer =
                        TimetableSpacer(startDifference).takeIf { startDifference != Duration.ZERO }
                    val endSpacer =
                        TimetableSpacer(endDifference).takeIf { endDifference != Duration.ZERO }

                    TimetableConflictItem(
                        spacerStart = startSpacer,
                        spacerEnd = endSpacer,
                        event = conflict,
                        start = event.start,
                        end = event.end
                    )
                }
                event.copy(conflictsWithSpacers)
            } else {
                event
            }
        }
    }

    private fun groupOverlappingEvents(events: List<TimetableEvent>): List<TimetableEvent> {
        val disjointSet = DisjointSet<TimetableEvent>()

        events.forEachIndexed { i, currentEvent ->
            events.subList(i + 1, events.size).forEach { otherEvent ->
                if (currentEvent.overlap(otherEvent)) {
                    disjointSet.union(currentEvent, otherEvent)
                }
            }
        }

        val groupedEvents = mutableMapOf<TimetableEvent, MutableList<TimetableEvent>>()

        events.forEach { event ->
            val root = disjointSet.find(event)
            groupedEvents.getOrPut(root) { mutableListOf() }.add(event)
        }

        return groupedEvents.values.map { event ->
            TimetableConflict(
                conflictedEvents = event,
                start = event.minOf { it.start },
                end = event.maxOf { it.end })
        }
    }

    private fun TimetableEvent.overlap(other: TimetableEvent) =
        start < other.end && end > other.start

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

    private data class RequestDateBounds(
        val from: LocalDate,
        val to: LocalDate
    )

    companion object {
        private const val spacerIdPrefix = "spacer_"
        private val schoolDayStart = 7.hours
        private val schoolDayEnd = 21.hours
    }
}