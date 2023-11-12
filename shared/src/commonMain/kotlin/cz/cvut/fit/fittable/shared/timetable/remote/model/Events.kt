package cz.cvut.fit.fittable.shared.timetable.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Events(
    @SerialName("events")
    val events: List<Event>,
    @SerialName("meta")
    val meta: Meta?,
)
