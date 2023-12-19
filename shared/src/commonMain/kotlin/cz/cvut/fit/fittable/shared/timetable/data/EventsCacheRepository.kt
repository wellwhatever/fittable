package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.timetable.data.local.EventsLocalDataSource
import cz.cvut.fit.fittable.shared.timetable.data.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Event
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EventsCacheRepository internal constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val usernameLocalDataSource: UsernameLocalDataSource,
    private val eventsRoute: EventsRoute,
) {

    suspend fun getCachedEvents(
        from: LocalDate,
        to: LocalDate,
    ) = getCachedEventsInRange(from, to)

    suspend fun getPersonalEvents(
        from: LocalDate,
        to: LocalDate,
    ): List<Event> {
        val username = usernameLocalDataSource.usernameFlow.first()
        val events = eventsRoute.getPersonEvents(username, from, to)
        eventsLocalDataSource.refreshEvents(events.events)

        return getCachedEventsInRange(from, to)
    }

    private suspend fun getCachedEventsInRange(from: LocalDate, to: LocalDate): List<Event> {
        val cachedEvents = eventsLocalDataSource.getEventsFlow().firstOrNull().orEmpty()
        val selectedRange = from..< to
        val cachedEventsInRange = cachedEvents.filter {
            val starts = it.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val ends = it.endsAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
            starts in selectedRange && ends in selectedRange
        }
        return cachedEventsInRange
    }

    suspend fun getUserEvents(
        username: String,
        from: LocalDate,
        to: LocalDate,
    ): List<Event> {
        val events = eventsRoute.getPersonEvents(username, from, to)
        eventsLocalDataSource.refreshEvents(events.events)

        return eventsLocalDataSource.getEventsFlow().firstOrNull().orEmpty()
    }

    suspend fun getRoomEvents(
        room: String,
        from: LocalDate,
        to: LocalDate
    ): List<Event> = eventsRoute.getRoomEvents(room, from, to).events

    suspend fun getCoursesEvents(
        course: String,
        from: LocalDate,
        to: LocalDate
    ): List<Event> = eventsRoute.getCourseEvents(course, from, to).events

    suspend fun getEvent(eventId: String): EventDetail = eventsRoute.getEvent(eventId)

    suspend fun getUser(username: String): User = eventsRoute.getPersonInformation(username)
}
