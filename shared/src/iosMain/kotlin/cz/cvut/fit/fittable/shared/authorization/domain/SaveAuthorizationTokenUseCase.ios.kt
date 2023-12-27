package cz.cvut.fit.fittable.shared.authorization.domain

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.core.remote.ApiException
import kotlin.coroutines.cancellation.CancellationException

actual class SaveAuthorizationTokenUseCase(
    private val authorizationLocalDataSource: AuthorizationLocalDataSource,
    private val saveUsernameUseCase: SaveUsernameUseCase
) {
    @Throws(CancellationException::class, ApiException::class)
    actual suspend operator fun invoke(rawData: String) {
        authorizationLocalDataSource.updateAuthorizationToken(rawData)
        saveUsernameUseCase()
    }
}
