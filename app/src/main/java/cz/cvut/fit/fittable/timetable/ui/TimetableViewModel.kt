package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.timetable.domain.model.CalendarGridHour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimetableViewModel(
    generateHoursGrid: GenerateHoursGridUseCase
) : ViewModel() {
    private val hours = MutableStateFlow<List<CalendarGridHour>?>(null)

    val uiState = hours.map {
        if (it.isNullOrEmpty()) {
            TimetableLoading
        } else {
            TimetableContent(it)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )

    init {
        viewModelScope.launch {
            hours.value = generateHoursGrid()
        }
    }
}


sealed interface TimetableUiState

data class TimetableContent(
    val hoursGrid: List<CalendarGridHour>
) : TimetableUiState

data object TimetableLoading : TimetableUiState
