package cz.cvut.fit.fittable.shared.core.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.formatAsHoursAndMinutes(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return dateTime.time.toString().substring(0, 5)
}