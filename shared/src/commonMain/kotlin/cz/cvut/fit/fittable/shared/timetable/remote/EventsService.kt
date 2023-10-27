package cz.cvut.fit.fittable.shared.timetable.remote

import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class EventsService internal constructor(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {
    private val eventsRoute = "/events"
    suspend fun getEvents(): Events = httpClient.get("$baseUrl$eventsRoute").body()
}
