package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class CalendarHeaderItem(
    val startMonth: LocalDate,
    val endMonth: LocalDate,
    val today: Instant,
    val selected: Instant,
)
