package cz.cvut.fit.fittable.shared.authorization.di

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRoute
import cz.cvut.fit.fittable.shared.authorization.domain.SaveUsernameUseCase
import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authorizationModule = module {
    singleOf(::SaveUsernameUseCase)
    single(named("auth")) {
        NetworkClient(
            baseUrl = "https://auth.fit.cvut.cz/oauth/",
            httpClient = get(named("api")),
        )
    }
    single { AuthorizationRoute(get(named("auth"))) }

    single { AuthorizationLocalDataSource(get(named("authorization"))) }
    single { UsernameLocalDataSource(get(named("username"))) }
    single(named("api")) {
        provideAuthHttpClient(
            get(),
            get(named("default")),
        )
    }
}
