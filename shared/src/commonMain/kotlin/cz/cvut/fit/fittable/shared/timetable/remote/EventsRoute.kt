package cz.cvut.fit.fittable.shared.timetable.remote

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.timetable.remote.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import cz.cvut.fit.fittable.shared.timetable.remote.model.User
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import kotlinx.datetime.LocalDate

internal class EventsRoute(
    private val client: NetworkClient,
) {
    private val eventsRoute = "events"
    private val peopleRoute = "people"
    private val teachersRoute = "teachers"
    private val roomsRoute = "rooms"

    suspend fun getEvent(eventId: String): EventDetail = client.request(
        path = "$eventsRoute/$eventId",
        method = HttpMethod.Get
    )

    suspend fun getPersonEvents(username: String, from: LocalDate, to: LocalDate): Events =
        client.request(
            path = "$peopleRoute/$username/$eventsRoute",
            method = HttpMethod.Get,
        ) {
            url {
                val values = StringValues.build {
                    append("from", from.toString())
                    append("to", to.toString())
                }
                parameters.appendAll(values)
            }
        }

    suspend fun getPersonInformation(username: String): User = client.request(
        path = "$peopleRoute/$username",
        method = HttpMethod.Get
    )

    suspend fun getTeachersEvents(username: String): Events = client.request(
        path = "$teachersRoute/$username/$eventsRoute",
        method = HttpMethod.Get,
    )

    suspend fun getRooms(kosId: String): Events = client.request(
        path = "$roomsRoute/$kosId/$eventsRoute",
        method = HttpMethod.Get,
    )
}
