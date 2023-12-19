package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.CalendarBounds
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

class GetTimetableCalendarBoundsUseCase {
    operator fun invoke(): CalendarBounds {
        val now = Clock.System.now()
        val todayDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return CalendarBounds(
            monthStart = LocalDate(2000, 1, 1),
            monthEnd = (now + 365.days).toLocalDateTime(TimeZone.currentSystemDefault()).date,
            today = todayDate,
        )
    }
}