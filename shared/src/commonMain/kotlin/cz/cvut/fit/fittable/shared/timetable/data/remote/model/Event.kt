package cz.cvut.fit.fittable.shared.timetable.data.remote.model

import cz.cvut.fit.fittable.shared.timetable.data.remote.serializer.InstantAsStringSerializer
import cz.cvut.fit.fittable.shared.timetable.data.remote.serializer.OptionalSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("capacity")
    val capacity: Int? = null,
    @SerialName("deleted")
    val deleted: Boolean,
    @SerialName("ends_at")
    @Serializable(with = InstantAsStringSerializer::class)
    val endsAt: Instant,
    @SerialName("event_type")
    val eventType: EventType,
    @SerialName("id")
    val id: Long,
    @SerialName("links")
    val links: Links? = null,
    @SerialName("name")
    @Serializable(with = OptionalSerializer::class)
    val name: String? = null,
    @SerialName("occupied")
    val occupied: Int,
    @SerialName("original_data")
    val originalData: OriginalData? = null,
    @SerialName("parallel")
    val parallel: String,
    @SerialName("sequence_number")
    val sequenceNumber: Int,
    @SerialName("starts_at")
    @Serializable(with = InstantAsStringSerializer::class)
    val startsAt: Instant,
)
