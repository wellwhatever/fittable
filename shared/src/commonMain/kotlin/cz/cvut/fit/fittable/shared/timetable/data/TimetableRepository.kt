package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.remote.EventsService

class TimetableRepository(
    private val eventsService: EventsService,
) {
    suspend fun getEvents() = eventsService.getEvents()
}
