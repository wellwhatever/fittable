package cz.cvut.fit.fittable.shared.timetable.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDetail(
    @SerialName("events")
    val event: Event
)
