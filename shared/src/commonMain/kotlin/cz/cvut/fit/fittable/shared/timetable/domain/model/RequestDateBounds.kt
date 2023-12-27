package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.LocalDate

internal data class RequestDateBounds(
    val from: LocalDate,
    val to: LocalDate
)
