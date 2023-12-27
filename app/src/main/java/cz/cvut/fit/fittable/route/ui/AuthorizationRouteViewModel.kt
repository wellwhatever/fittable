package cz.cvut.fit.fittable.route.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.route.domain.AuthorizationState
import cz.cvut.fit.fittable.route.domain.GetAuthorizationStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AuthorizationRouteViewModel(
    getAuthorizationState: GetAuthorizationStateUseCase,
) : ViewModel() {
    val uiState: StateFlow<AuthorizationRouteUiState> =
        getAuthorizationState().map {
            when (it) {
                is AuthorizationState.Authorized -> AuthorizationRouteUiState.Authorized
                AuthorizationState.Unauthorized -> AuthorizationRouteUiState.Unauthorized
                AuthorizationState.UnauthorizedOffline -> AuthorizationRouteUiState.Offline
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AuthorizationRouteUiState.Loading
        )
}

@Stable
sealed interface AuthorizationRouteUiState {
    data object Authorized : AuthorizationRouteUiState
    data object Unauthorized : AuthorizationRouteUiState
    data object Offline : AuthorizationRouteUiState

    data object Loading : AuthorizationRouteUiState
}
