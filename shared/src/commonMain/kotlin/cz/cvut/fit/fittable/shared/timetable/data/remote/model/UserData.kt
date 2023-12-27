package cz.cvut.fit.fittable.shared.timetable.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("id")
    val id: String
)
