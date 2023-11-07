package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour

class GenerateHoursGridUseCase {
    private val hourRange = 7..21
    operator fun invoke(): List<TimetableHour> = hourRange.map { hour ->
        TimetableHour("$hour:00")
    }
}