package cz.cvut.fit.fittable.shared.timetable.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("people")
    val people: UserData
)