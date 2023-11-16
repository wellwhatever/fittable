package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events

class EventsConverterRemote {
    fun toDomain(remote: Events): List<EventDomain> = remote.events.map { remoteEvent ->
        EventDomain(
            title = remoteEvent.links?.course.orEmpty(),
            room = remoteEvent.links?.room.orEmpty(),
            start = remoteEvent.startsAt,
            end = remoteEvent.endsAt,
        )
    }
}