package cz.cvut.fit.fittable.shared.timetable.remote

import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import cz.cvut.fit.fittable.shared.timetable.remote.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PeopleService internal constructor(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {
//    suspend fun getUserInfo(): User = httpClient.get("$baseUrl/")
    suspend fun getEvents(): Events = httpClient.get("$baseUrl").body()
}
