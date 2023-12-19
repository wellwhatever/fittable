package cz.cvut.fit.fittable.shared.timetable.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    @SerialName("applied_exceptions")
    val appliedExceptions: List<Int> = emptyList(),
    @SerialName("course")
    val course: String? = null,
    @SerialName("room")
    val room: String? = null,
    @SerialName("students")
    val students: List<String> = emptyList(),
    @SerialName("teachers")
    val teachers: List<String> = emptyList(),
)
