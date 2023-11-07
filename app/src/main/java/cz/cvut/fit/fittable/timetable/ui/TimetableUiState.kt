package cz.cvut.fit.fittable.timetable.ui

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem

sealed interface TimetableUiState

data class TimetableContent(
    val hoursGrid: List<TimetableHour>,
    val events: List<TimetableItem>
) : TimetableUiState

data object TimetableLoading : TimetableUiState