package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.timetable.data.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Events
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.User
import kotlinx.datetime.LocalDate

class EventsRepository internal constructor(
    private val eventsRoute: EventsRoute,
) {
    // TODO hardcoded for now until appManager will give permission to fetch username
    suspend fun getUserEvents(
        username: String = "petrool2",
        from: LocalDate,
        to: LocalDate,
    ): Events = eventsRoute.getPersonEvents(username, from, to)

    suspend fun getRoomEvents(
        room: String,
        from: LocalDate,
        to: LocalDate
    ): Events = eventsRoute.getRoomEvents(room, from, to)

    suspend fun getCoursesEvents(
        course: String,
        from: LocalDate,
        to: LocalDate
    ): Events = eventsRoute.getCourseEvents(course, from, to)

    suspend fun getEvent(eventId: String): EventDetail = eventsRoute.getEvent(eventId)

    suspend fun getUser(username: String): User = eventsRoute.getPersonInformation(username)
}
