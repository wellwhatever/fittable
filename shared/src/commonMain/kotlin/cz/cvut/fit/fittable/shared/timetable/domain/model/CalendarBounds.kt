package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.LocalDate

data class CalendarBounds(
    val monthStart: LocalDate,
    val monthEnd: LocalDate,
    val today: LocalDate,
)
