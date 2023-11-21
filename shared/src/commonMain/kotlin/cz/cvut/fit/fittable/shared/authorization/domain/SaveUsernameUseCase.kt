package cz.cvut.fit.fittable.shared.authorization.domain

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRoute
import kotlinx.coroutines.flow.first

class SaveUsernameUseCase internal constructor(
    private val authorizationRoute: AuthorizationRoute,
    private val authorizationLocalDataSource: AuthorizationLocalDataSource,
    private val usernameLocalDataSource: UsernameLocalDataSource
) {
    suspend operator fun invoke() {
        val token = authorizationLocalDataSource.authorizationTokenFlow.first()
        val username = authorizationRoute.getUserInformation(token)
        usernameLocalDataSource.updateUsername(username.userName)
    }
}