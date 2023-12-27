package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.domain.SaveAuthorizationTokenUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

object AuthorizationDependencyProvider : KoinComponent {
    val saveAuthorizationTokenUseCase: SaveAuthorizationTokenUseCase by inject()
}
