package cz.cvut.fit.fittable.shared.timetable.data.local.converter

import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Event
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventType
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Links
import cz.cvut.fit.fittable.sqldelight.EventEntity
import kotlinx.datetime.Instant

class EventConverterLocal {
    fun toRemote(local: EventEntity): Event =
        with(local) {
            Event(
                id = id.toInt(),
                links = Links(
                    course = local.title,
                    room = local.room
                ),
                startsAt = Instant.parse(start),
                endsAt = Instant.parse(ends),
                deleted = deleted,
                eventType = EventType.valueOf(eventType),
                occupied = occupied.toInt(),
                parallel = parallel,
                sequenceNumber = sequenceNumber.toInt(),
            )
        }

    fun toEntity(remote: Event): EventEntity =
        with(remote) {
            EventEntity(
                id = id.toLong(),
                title = links?.course.orEmpty(),
                room = links?.room.orEmpty(),
                start = startsAt.toString(),
                ends = endsAt.toString(),
                deleted = deleted,
                eventType = eventType.name,
                occupied = occupied.toLong(),
                parallel = parallel,
                sequenceNumber = sequenceNumber.toLong(),
            )
        }
}