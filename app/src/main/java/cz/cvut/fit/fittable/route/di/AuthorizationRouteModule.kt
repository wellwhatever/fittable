package cz.cvut.fit.fittable.route.di

import cz.cvut.fit.fittable.route.domain.GetAuthorizationStateUseCase
import cz.cvut.fit.fittable.route.ui.AuthorizationRouteViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authorizationRouteModule = module {
    factoryOf(::GetAuthorizationStateUseCase)
    viewModelOf(::AuthorizationRouteViewModel)
}
