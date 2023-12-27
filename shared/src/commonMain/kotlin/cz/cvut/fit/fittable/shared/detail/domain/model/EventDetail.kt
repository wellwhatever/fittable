package cz.cvut.fit.fittable.shared.detail.domain.model

import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class EventDetail(
    val course: String,
    val room: String,
    val sequenceNumber: String,
    val capacity: Int,
    val occupied: Int,
    val eventType: EventType,
    val parallel: String,
    val teacherUsernames: List<String>,
    val starts: Instant,
    val ends: Instant
) {
    val startsDate = starts.toLocalDateTime(TimeZone.currentSystemDefault()).date
}
