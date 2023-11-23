package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.runtime.Stable
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.CalendarBounds
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


    sealed interface Error : TimetableUiState {
        data object NoPermission : Error
        data object UnknownError : Error
        data object Unauthorized : Error
    }

    data object Loading : TimetableUiState
}

@Stable
data class HeaderState(
    val calendarBounds: CalendarBounds,
    val selectedDate: LocalDate,
)

internal fun ApiException.mapTimetableException(): TimetableUiState.Error =
    when {
        this is HttpExceptionDomain && code == 403 -> {
            TimetableUiState.Error.NoPermission
        }

        this is HttpExceptionDomain && code == 400 -> {
            TimetableUiState.Error.Unauthorized
        }

        else -> TimetableUiState.Error.UnknownError
    }