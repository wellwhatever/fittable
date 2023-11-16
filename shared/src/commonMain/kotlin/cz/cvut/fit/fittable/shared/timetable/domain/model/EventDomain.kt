package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.Instant

data class EventDomain(
    val title: String,
    val room: String,
    val start: Instant,
    val end: Instant,
)

data class MergedEvents(
    val events: List<EventDomain>,
    val start: Instant,
    val end: Instant,
)