package cz.cvut.fit.fittable.shared.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun todayDate(): LocalDate {
    val now = Clock.System.now()
    return now.toLocalDateTime(TimeZone.currentSystemDefault()).date
}
