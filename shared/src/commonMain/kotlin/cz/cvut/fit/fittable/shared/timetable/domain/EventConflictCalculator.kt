package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.MergedEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class EventConflictCalculator {
    suspend fun groupOverlappingEvents(events: List<EventDomain>): List<MergedEvents> =
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

    private fun EventDomain.overlap(other: EventDomain) =
        start < other.end && end > other.start
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