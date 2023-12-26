package cz.cvut.fit.fittable.shared.timetable.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType {
    @SerialName("tutorial")
    TUTORIAL,

    @SerialName("laboratory")
    LABORATORY,

    @SerialName("lecture")
    LECTURE,

    @SerialName("assessment")
    ASSESSMENT,

    @SerialName("exam")
    EXAM,

    @SerialName("course_event")
    EVENT
}