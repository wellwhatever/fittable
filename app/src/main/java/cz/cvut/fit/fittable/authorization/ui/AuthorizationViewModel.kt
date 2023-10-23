package cz.cvut.fit.fittable.authorization.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.authorization.domain.CreateLoginRequestUseCase
import cz.cvut.fit.fittable.authorization.domain.SaveAuthorizationTokenUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest

interface AuthorizationScreenActions {
    fun onAuthorizeClick()
    fun onReceiveTokenSuccess(data: String)
    fun onReceiveTokenError(error: String)
}

class AuthorizationViewModel internal constructor(
    private val createLoginRequestUseCase: CreateLoginRequestUseCase,
    private val saveAuthorizationTokenUseCase: SaveAuthorizationTokenUseCase,
) : ViewModel(), AuthorizationScreenActions {
    private val _navigateToAuthorization = Channel<AuthorizationRequest>(Channel.CONFLATED)
    val navigateToAuthorization = _navigateToAuthorization.receiveAsFlow()

    private val _navigateToTimetableScreen = Channel<String>(Channel.CONFLATED)
    val navigateToTimetableScreen = _navigateToTimetableScreen.receiveAsFlow()

    override fun onAuthorizeClick() {
        viewModelScope.launch {
            _navigateToAuthorization.send(createLoginRequestUseCase())
        }
    }

    override fun onReceiveTokenSuccess(data: String) {
        viewModelScope.launch {
            saveAuthorizationTokenUseCase(data)
            _navigateToTimetableScreen.send(data)
        }
    }

    override fun onReceiveTokenError(error: String) {
        TODO("onActivityResultError display error here")
    }
}
