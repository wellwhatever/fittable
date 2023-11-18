package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.remote.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import cz.cvut.fit.fittable.shared.timetable.remote.model.User
import kotlinx.datetime.LocalDate

class EventsRepository internal constructor(
    private val eventsRoute: EventsRoute,
) {
    suspend fun getUserEvents(from: LocalDate, to: LocalDate): Events {
        // TODO hardcoded for now until appManager will give permission to fetch username
        val username = "petrool2"
        return eventsRoute.getPersonEvents(username, from, to)
    }

    suspend fun getEvent(eventId: String): EventDetail = eventsRoute.getEvent(eventId)

    suspend fun getUser(username: String): User = eventsRoute.getPersonInformation(username)
}
