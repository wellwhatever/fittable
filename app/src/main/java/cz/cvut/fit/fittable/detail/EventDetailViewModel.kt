package cz.cvut.fit.fittable.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import cz.cvut.fit.fittable.shared.detail.domain.GetTeacherUseCase
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.detail.domain.model.Teacher
import cz.cvut.fit.fittable.timetable.navigation.EventDetailArgs
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

    private val error = MutableStateFlow<ApiException?>(null)
    private val eventDetail = MutableStateFlow<EventDetail?>(null)
    private val teachers = MutableStateFlow<List<Teacher>?>(null)

    val uiState: StateFlow<EventDetailState> = combine(
        eventDetail,
        error
    ) { event, error ->
        // TODO fix teachers when scopes will be received from CTU
        when {
            error != null -> EventDetailState.Error
            event == null -> EventDetailState.Loading
            else -> EventDetailState.Content(event, emptyList())
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
                    // TODO fix teachers when scopes will be received from CTU
//                    val teachersAsync = event.teacherUsernames.map { username ->
//                        async {
//                            getTeacher(username)
//                        }
//                    }
//                    teachers.value = teachersAsync.awaitAll()
                }
            } catch (exception: ApiException) {
                error.value = exception
            }
        }
    }

    fun onReloadClick() {
        error.value = null
        fetchEventDetails()
    }
}

sealed interface EventDetailState {
    data class Content(
        val eventDetail: EventDetail,
        val teachers: List<Teacher>
    ) : EventDetailState

    data object Error : EventDetailState
    data object Loading : EventDetailState
}
