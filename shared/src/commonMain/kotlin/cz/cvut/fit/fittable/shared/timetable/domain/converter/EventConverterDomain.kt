package cz.cvut.fit.fittable.shared.timetable.domain.converter

import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSingleEvent

class EventConverterDomain {
    fun toTimetableItem(domain: EventDomain) = with(domain) {
        TimetableSingleEvent(
            title = title,
            room = room,
            start = start,
            end = end,
            id = id
        )
    }
}
