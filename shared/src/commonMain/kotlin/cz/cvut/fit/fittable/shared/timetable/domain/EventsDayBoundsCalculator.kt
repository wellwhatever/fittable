package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.RequestDateBounds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours

internal class EventsDayBoundsCalculator {
    fun getRequestDataBounds(day: LocalDate): RequestDateBounds {
        val defaultTimeZone = TimeZone.currentSystemDefault()
        val startOfDayInstant = day
            .atTime(0, 0, 0)
            .toInstant(defaultTimeZone)
        val endOfDayInstant = startOfDayInstant + 24.hours

        val startDate = startOfDayInstant.toLocalDateTime(defaultTimeZone).date
        val endDate = endOfDayInstant.toLocalDateTime(defaultTimeZone).date
        return RequestDateBounds(startDate, endDate)
    }
}