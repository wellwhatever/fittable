package cz.cvut.fit.fittable.shared.timetable.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType {
    @SerialName("tutorial")
    TUTORIAL,

    @SerialName("laboratory")
    LABORATORY,

    @SerialName("lecture")
    LECTURE
}