package cz.cvut.fit.fittable.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import cz.cvut.fit.fittable.shared.detail.domain.GetTeacherUseCase
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.detail.domain.model.Teacher
import cz.cvut.fit.fittable.timetable.navigation.EventDetailArgs
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val getTeacher: GetTeacherUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val topicArgs: EventDetailArgs = EventDetailArgs(savedStateHandle)

    private val error = MutableStateFlow<String?>(null)
    private val eventDetail = MutableStateFlow<EventDetail?>(null)
    private val teachers = MutableStateFlow<List<Teacher>?>(null)

    val uiState: StateFlow<EventDetailState> = combine(
        eventDetail,
        teachers,
        error
    ) { event, teachers, error ->
        when {
            error != null -> EventDetailState.Error(error)
            event == null || teachers == null -> EventDetailState.Loading
            else -> EventDetailState.Content(event, teachers)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EventDetailState.Loading,
    )

    init {
        fetchEventDetails()
    }

    private fun fetchEventDetails() {
        viewModelScope.launch {
            try {
                eventDetail.value = getEventById(topicArgs.eventId).also { event ->
                    val teachersAsync = event.teacherUsernames.map { username ->
                        async {
                            getTeacher(username)
                        }
                    }
                    teachers.value = teachersAsync.awaitAll()
                }
            } catch (exception: HttpExceptionDomain) {
                error.value = exception.message
            }
        }
    }
}


sealed interface EventDetailState {
    data class Content(
        val eventDetail: EventDetail,
        val teachers: List<Teacher>
    ) : EventDetailState

    data class Error(val message: String) : EventDetailState
    data object Loading : EventDetailState
}