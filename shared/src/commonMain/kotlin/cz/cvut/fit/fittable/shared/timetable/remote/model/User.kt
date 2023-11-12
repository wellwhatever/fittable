package cz.cvut.fit.fittable.shared.timetable.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val username: String,
    @SerialName("full_name")
    val name: String,
    @SerialName("access_token")
    val string: String,
)
