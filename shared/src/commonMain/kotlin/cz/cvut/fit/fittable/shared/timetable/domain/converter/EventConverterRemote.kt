package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Event
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain

class EventConverterRemote {
    fun toDomain(remote: Event): EventDomain =
        with(remote) {
            EventDomain(
                title = links?.course.orEmpty(),
                room = links?.room.orEmpty(),
                start = startsAt,
                end = endsAt,
                id = id.toString()
            )
        }
}