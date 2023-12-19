package cz.cvut.fit.fittable.shared.authorization.data.remote

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import kotlinx.coroutines.flow.Flow

class AuthorizationRepository(
    private val authorizationRoute: AuthorizationRoute,
    authorizationLocalDataSource: AuthorizationLocalDataSource,
) {
    val authorizationToken: Flow<String?> =
        authorizationLocalDataSource.authorizationTokenFlow

    suspend fun getTokenExpiration(token: String): Long =
        authorizationRoute.getTokenInformation(token).exp

    fun extractAuthorizationToken(data: String) = getAccessTokenFromUri(data)

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
