package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events

class EventsConverterRemote {
    fun toDomain(remote: Events): List<TimetableEvent> = remote.events.map { remoteEvent ->
        TimetableEvent(
            title = remoteEvent.links?.course.orEmpty(),
            room = remoteEvent.links?.room.orEmpty(),
            duration = remoteEvent.endsAt - remoteEvent.startsAt,
            start = remoteEvent.startsAt,
            end = remoteEvent.endsAt,
            id = remoteEvent.id.toString()
        )
    }
}