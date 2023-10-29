package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.remote.EventsRoute

class TimetableRepository internal constructor(
    private val eventsRoute: EventsRoute,
) {
    suspend fun getEvents() = eventsRoute.getAllEvents()
}
