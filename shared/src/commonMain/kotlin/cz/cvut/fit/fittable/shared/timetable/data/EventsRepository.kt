package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.remote.model.Event
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import kotlinx.datetime.LocalDate

class EventsRepository internal constructor(
    private val eventsRoute: EventsRoute,
) {
    suspend fun getEvents() = eventsRoute.getEvents()

    suspend fun getUserEvents(from: LocalDate, to: LocalDate): Events {
        // TODO hardcoded for now until appManager will give permission to fetch username
        val username = "petrool2"
        return eventsRoute.getPersonEvents(username, from, to)
    }

    suspend fun getEvent(eventId: String): Event = eventsRoute.getEvent(eventId)
}
