package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.timetable.data.TimetableRepository
import cz.cvut.fit.fittable.shared.timetable.remote.model.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimetableViewModel(
    timetableRepository: TimetableRepository,
) : ViewModel() {
    private val events = MutableStateFlow<Events?>(null)

    val uiState = events.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )

    init {
        viewModelScope.launch {
            events.value = timetableRepository.getEvents()
        }
    }
}
