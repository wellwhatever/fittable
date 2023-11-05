package cz.cvut.fit.fittable.timetable.domain

import cz.cvut.fit.fittable.timetable.domain.model.TimetableGridHour

class GenerateHoursGridUseCase {
    private val hourRange = 7..21
    operator fun invoke(): List<TimetableGridHour> = hourRange.map { hour ->
        TimetableGridHour("$hour:00")
    }
}