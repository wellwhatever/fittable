package cz.cvut.fit.fittable.timetable.domain

import cz.cvut.fit.fittable.timetable.domain.model.CalendarGridHour

class GenerateHoursGridUseCase {
    private val hourRange = 7..21
    operator fun invoke(): List<CalendarGridHour> = hourRange.map { hour ->
        CalendarGridHour("$hour:00")
    }
}