package cz.cvut.fit.fittable.timetable.ui

import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem

sealed interface TimetableUiState {
    data class Content(
        val hoursGrid: List<TimetableHour>,
        val events: List<TimetableItem>
    ) : TimetableUiState

    data class Error(
        val error: String
    ) : TimetableUiState

    data object Loading : TimetableUiState
}