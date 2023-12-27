package cz.cvut.fit.fittable.shared.authorization.data.remote

import cz.cvut.fit.fittable.shared.authorization.data.remote.model.TokenInformation
import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import io.ktor.http.HttpMethod

class AuthorizationRoute internal constructor(
    private val client: NetworkClient,
) {
    suspend fun getTokenInformation(token: String): TokenInformation = client.request(
        path = checkTokenRoute,
        method = HttpMethod.Get
    ) {
        url {
            parameters.append("token", token)
        }
    }

    companion object {
        private const val checkTokenRoute = "check_token"
    }
}
