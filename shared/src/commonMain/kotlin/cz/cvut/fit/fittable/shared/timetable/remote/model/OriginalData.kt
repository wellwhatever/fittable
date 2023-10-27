package cz.cvut.fit.fittable.shared.timetable.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OriginalData(
    @SerialName("ends_at")
    val endsAt: String? = null,
    @SerialName("room_id")
    val roomId: String? = null,
    @SerialName("starts_at")
    val startsAt: String? = null,
)
