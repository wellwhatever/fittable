package cz.cvut.fit.fittable.authorization.data

import android.net.Uri
import cz.cvut.fit.fittable.app.di.AuthorizationConfiguration
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AuthorizationRequestComposer(
    private val authorizationConfiguration: AuthorizationConfiguration,
    private val authorizationServiceConfiguration: AuthorizationServiceConfiguration,
) {
    fun composeAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.Builder(
            authorizationServiceConfiguration,
            authorizationConfiguration.clientId,
            ResponseTypeValues.TOKEN,
            Uri.parse(authorizationConfiguration.redirectUri),
        ).setScope(authorizationConfiguration.scope).build()
}