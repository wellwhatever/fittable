package cz.cvut.fit.fittable.authorization.domain

import cz.cvut.fit.fittable.authorization.data.AuthorizationRepository
import net.openid.appauth.AuthorizationRequest

internal class CreateLoginRequestUseCase(
    private val authorizationRepository: AuthorizationRepository,
) {
    operator fun invoke(): AuthorizationRequest =
        authorizationRepository.composeAuthorizationRequest()
}
