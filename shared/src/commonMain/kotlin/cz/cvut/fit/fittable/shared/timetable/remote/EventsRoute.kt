package cz.cvut.fit.fittable.shared.timetable.remote

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import io.ktor.http.HttpMethod

internal class EventsRoute(
    private val client: NetworkClient,
) {
    private val eventsRoute = "events"
    private val peopleRoute = "people"
    private val teachersRoute = "teachers"
    private val roomsRoute = "rooms"

    suspend fun getAllEvents(): Events = client.request(
        path = eventsRoute,
        method = HttpMethod.Get,
    )

    suspend fun getPersonEvents(username: String): Events = client.request(
        path = "$peopleRoute/$username/$eventsRoute",
        method = HttpMethod.Get,
    )

    suspend fun getTeachersRoute(username: String): Events = client.request(
        path = "$teachersRoute/$username/$eventsRoute",
        method = HttpMethod.Get,
    )

    suspend fun getRoomsRoute(kosId: String): Events = client.request(
        path = "$roomsRoute/$kosId/$eventsRoute",
        method = HttpMethod.Get,
    )
}
