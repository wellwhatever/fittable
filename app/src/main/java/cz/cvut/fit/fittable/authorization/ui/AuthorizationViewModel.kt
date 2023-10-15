package cz.cvut.fit.fittable.authorization.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.authorization.data.AuthorizationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest

interface AuthorizationScreenActions {
    fun onAuthorizeClick()
    fun onReceiveTokenSuccess(data: String)
    fun onReceiveTokenError(error: String)
}

class AuthorizationViewModel(
    private val authorizationRepository: AuthorizationRepository,
) : ViewModel(), AuthorizationScreenActions {
    private val _navigateToAuthorization = Channel<AuthorizationRequest>(Channel.CONFLATED)
    val navigateToAuthorization = _navigateToAuthorization.receiveAsFlow()
    override fun onAuthorizeClick() {
        viewModelScope.launch {
            _navigateToAuthorization.send(authorizationRepository.composeAuthorizationRequest())
        }
    }

    override fun onReceiveTokenSuccess(data: String) {
        val token = authorizationRepository.extractAuthorizationToken(data)
        Log.e("onReceiveTokenSuccess:", "token:$token")
    }

    override fun onReceiveTokenError(error: String) {
        TODO("onActivityResultError display error here")
    }
}
