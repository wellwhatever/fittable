package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events

class EventsConverterRemote {
    fun toDomain(remote: Events): List<EventDomain> = remote.events.map { remoteEvent ->
        with(remoteEvent) {
            EventDomain(
                title = links?.course.orEmpty(),
                room = links?.room.orEmpty(),
                start = startsAt,
                end = endsAt,
                id = id.toString()
            )
        }
    }
}