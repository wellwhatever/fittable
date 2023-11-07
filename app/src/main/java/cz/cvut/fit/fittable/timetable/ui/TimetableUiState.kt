package cz.cvut.fit.fittable.timetable.ui

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableGridHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem

sealed interface TimetableUiState

data class TimetableContent(
    val hoursGrid: List<TimetableGridHour>,
    val events: List<TimetableItem>
) : TimetableUiState

data object TimetableLoading : TimetableUiState