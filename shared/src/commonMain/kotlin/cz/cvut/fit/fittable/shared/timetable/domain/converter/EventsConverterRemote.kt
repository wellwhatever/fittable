package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSingleEvent
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events

class EventsConverterRemote {
    fun toDomain(remote: Events): List<TimetableSingleEvent> = remote.events.map { remoteEvent ->
        TimetableSingleEvent(
            title = remoteEvent.links?.course.orEmpty(),
            room = remoteEvent.links?.room.orEmpty(),
            start = remoteEvent.startsAt,
            end = remoteEvent.endsAt,
        )
    }
}