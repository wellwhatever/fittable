package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import kotlinx.datetime.LocalDate

class TimetableRepository internal constructor(
    private val eventsRoute: EventsRoute,
) {
    suspend fun getEvents() = eventsRoute.getAllEvents()

    suspend fun getUserEventsForDay(from: LocalDate, to: LocalDate): Events {
        // TODO hardcoded for now until appManager will give permission to fetch username
        val username = "petrool2"
        return eventsRoute.getPersonEvents(username, from, to)
    }
}
