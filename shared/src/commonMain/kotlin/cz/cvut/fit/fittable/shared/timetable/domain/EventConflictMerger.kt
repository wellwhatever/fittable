package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.MergedEvents
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflictContent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEventContainer
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

internal class EventConflictMerger(
    private val eventConverterDomain: EventConverterDomain
) {

    suspend fun mergeConflicts(events: List<EventDomain>, day: LocalDate): List<TimetableItem> {
        // We have to merge conflicted events as they're considered as single element
        val mergedEvents = groupOverlappingEvents(events)
        val mergedEventsWithSeparators = composeTimetableItems(mergedEvents)
        return createTimetableWithSpacers(events = mergedEventsWithSeparators, day = day)
    }

    private suspend fun groupOverlappingEvents(events: List<EventDomain>): List<MergedEvents> =
        withContext(Dispatchers.Default) {
            val disjointSet = DisjointSet<EventDomain>()

            events.forEachIndexed { i, currentEvent ->
                events.subList(i + 1, events.size).forEach { otherEvent ->
                    if (currentEvent.overlap(otherEvent)) {
                        disjointSet.union(currentEvent, otherEvent)
                    }
                }
            }

            val groupedEvents = mutableMapOf<EventDomain, MutableList<EventDomain>>()

            events.forEach { event ->
                val root = disjointSet.find(event)
                groupedEvents.getOrPut(root) { mutableListOf() }.add(event)
            }

            return@withContext groupedEvents.values.map { event ->
                MergedEvents(
                    events = event,
                    start = event.minOf { it.start },
                    end = event.maxOf { it.end })
            }
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
                    event = eventConverterDomain.toTimetableItem(conflict)
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

    private fun EventDomain.overlap(other: EventDomain) =
        start < other.end && end > other.start

    companion object {
        private val schoolDayStart = 7.hours
        private val schoolDayEnd = 21.hours
    }
}

class DisjointSet<T> {
    private val parent = mutableMapOf<T, T>()

    fun find(event: T): T {
        if (!parent.containsKey(event)) {
            parent[event] = event
        }

        if (parent[event] != event) {
            parent[event] = find(parent[event]!!)
        }

        return parent[event]!!
    }

    fun union(event1: T, event2: T) {
        val root1 = find(event1)
        val root2 = find(event2)

        if (root1 != root2) {
            parent[root1] = root2
        }
    }
}