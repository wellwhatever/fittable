package cz.cvut.fit.fittable.authorization.domain

import cz.cvut.fit.fittable.authorization.data.AuthorizationRepository
import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.domain.SaveUsernameUseCase

class SaveAuthorizationTokenUseCase(
    private val authorizationRepository: AuthorizationRepository,
    private val authorizationLocalDataSource: AuthorizationLocalDataSource,
    private val saveUsernameUseCase: SaveUsernameUseCase
) {
    suspend operator fun invoke(rawData: String) {
        val token = authorizationRepository.extractAuthorizationToken(rawData)
        authorizationLocalDataSource.updateAuthorizationToken(token.orEmpty())
        saveUsernameUseCase()
    }
}
