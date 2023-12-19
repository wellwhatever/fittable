package cz.cvut.fit.fittable.shared.timetable.data.remote

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Events
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.User
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import kotlinx.datetime.LocalDate

internal class EventsRoute(
    private val client: NetworkClient,
) {
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
                    append("include", getEventsIncludeParams)
                    append("deleted", true.toString())
                }
                parameters.appendAll(values)
            }
        }

    suspend fun getRoomEvents(room: String, from: LocalDate, to: LocalDate): Events =
        client.request(
            path = "$roomsRoute/$room/$eventsRoute",
            method = HttpMethod.Get,
        ) {
            url {
                val values = StringValues.build {
                    append("from", from.toString())
                    append("to", to.toString())
                    append("include", getEventsIncludeParams)
                    append("deleted", true.toString())
                }
                parameters.appendAll(values)
            }
        }


    suspend fun getCourseEvents(course: String, from: LocalDate, to: LocalDate): Events =
        client.request(
            path = "$coursesRoute/$course/$eventsRoute",
            method = HttpMethod.Get,
        ) {
            url {
                val values = StringValues.build {
                    append("from", from.toString())
                    append("to", to.toString())
                    append("include", getEventsIncludeParams)
                    append("deleted", true.toString())
                }
                parameters.appendAll(values)
            }
        }

    suspend fun getPersonInformation(username: String): User = client.request(
        path = "$peopleRoute/$username",
        method = HttpMethod.Get
    )

    suspend fun getRooms(kosId: String): Events = client.request(
        path = "$roomsRoute/$kosId/$eventsRoute",
        method = HttpMethod.Get,
    )

    companion object {
        private const val eventsRoute = "events"
        private const val peopleRoute = "people"
        private const val teachersRoute = "teachers"
        private const val roomsRoute = "rooms"
        private const val coursesRoute = "courses"
        private const val getEventsIncludeParams = "courses,teachers,schedule_exceptions"
    }
}
