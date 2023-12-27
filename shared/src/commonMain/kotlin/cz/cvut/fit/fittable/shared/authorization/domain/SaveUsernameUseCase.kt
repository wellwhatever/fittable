package cz.cvut.fit.fittable.shared.authorization.domain

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRoute
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class SaveUsernameUseCase internal constructor(
    private val authorizationRoute: AuthorizationRoute,
    private val authorizationLocalDataSource: AuthorizationLocalDataSource,
    private val usernameLocalDataSource: UsernameLocalDataSource
) {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke() {
        val token = authorizationLocalDataSource.authorizationTokenFlow.first()
        val username = authorizationRoute.getTokenInformation(token)
        usernameLocalDataSource.updateUsername(username.userName)
    }
}
