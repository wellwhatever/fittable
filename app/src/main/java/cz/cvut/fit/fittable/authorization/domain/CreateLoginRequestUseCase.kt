package cz.cvut.fit.fittable.authorization.domain

import cz.cvut.fit.fittable.authorization.data.AuthorizationRequestComposer
import net.openid.appauth.AuthorizationRequest

internal class CreateLoginRequestUseCase(
    private val authorizationRequestComposer: AuthorizationRequestComposer
) {
    operator fun invoke(): AuthorizationRequest =
        authorizationRequestComposer.composeAuthorizationRequest()
}
