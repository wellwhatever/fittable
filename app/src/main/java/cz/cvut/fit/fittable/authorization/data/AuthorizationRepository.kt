package cz.cvut.fit.fittable.authorization.data

import android.net.Uri
import cz.cvut.fit.fittable.app.di.AuthorizationConfiguration
import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRoute
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AuthorizationRepository(
    private val authorizationConfiguration: AuthorizationConfiguration,
    private val authorizationServiceConfiguration: AuthorizationServiceConfiguration,
    private val authorizationRoute: AuthorizationRoute,
    authorizationLocalDataSource: AuthorizationLocalDataSource,
) {
    val authorizationToken: Flow<String?> =
        authorizationLocalDataSource.authorizationTokenFlow

    suspend fun getTokenExpiration(token: String): Long =
        authorizationRoute.getTokenInformation(token).exp

    fun extractAuthorizationToken(data: String) = getAccessTokenFromUri(data)
    fun composeAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.Builder(
            authorizationServiceConfiguration,
            authorizationConfiguration.clientId,
            ResponseTypeValues.TOKEN,
            Uri.parse(authorizationConfiguration.redirectUri),
        ).setScope(authorizationConfiguration.scope).build()

    private fun getAccessTokenFromUri(uri: String): String? {
        val params = uri.split("&")
        for (param in params) {
            val parts = param.split("=")
            if (parts.size == 2 && parts[0] == "access_token") {
                return parts[1]
            }
        }
        return null
    }
}
