package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.runtime.Stable
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.datetime.LocalDate

@Stable
sealed interface TimetableUiState {
    data class Content(
        val hoursGrid: List<TimetableHour>,
        val events: List<TimetableItem>,
        val header: HeaderState
    ) : TimetableUiState

    data class Error(
        val error: String
    ) : TimetableUiState

    data object Loading : TimetableUiState
}

@Stable
data class HeaderState(
    val monthStart: LocalDate,
    val monthEnd: LocalDate,
    val today: LocalDate,
    val selectedDate: LocalDate,
)