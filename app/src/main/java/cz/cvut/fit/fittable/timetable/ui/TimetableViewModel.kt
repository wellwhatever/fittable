package cz.cvut.fit.fittable.timetable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.authorization.data.AuthorizationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TimetableViewModel(
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {
    val uiState = authorizationRepository.getAuthorizationToken().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )
}
