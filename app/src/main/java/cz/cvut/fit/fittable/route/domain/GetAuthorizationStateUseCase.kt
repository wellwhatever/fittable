package cz.cvut.fit.fittable.route.domain

import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRepository
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class GetAuthorizationStateUseCase(
    private val authorizationRepository: AuthorizationRepository
) {
    operator fun invoke(): Flow<AuthorizationState> {
        return authorizationRepository.authorizationToken.map { token ->
            if (token.isNullOrBlank()) {
                AuthorizationState.Unauthorized
            } else {
                checkTokenValidity(token)
            }
        }
    }

    private suspend fun checkTokenValidity(token: String): AuthorizationState {
        return try {
            if (isTokenValid(authorizationRepository.getTokenExpiration(token))) {
                AuthorizationState.Authorized(token)
            } else {
                AuthorizationState.Unauthorized
            }
        } catch (exception: ApiException) {
            if (exception is HttpExceptionDomain) {
                AuthorizationState.Unauthorized
            } else {
                AuthorizationState.UnauthorizedOffline
            }
        }
    }

    private fun isTokenValid(expiration: Long): Boolean {
        val expirationInstant = Instant.fromEpochSeconds(expiration)
        val currentInstant: Instant = Clock.System.now()

        return currentInstant < expirationInstant
    }
}

sealed interface AuthorizationState {
    data class Authorized(val token: String) : AuthorizationState

    data object Unauthorized : AuthorizationState
    data object UnauthorizedOffline : AuthorizationState
}
