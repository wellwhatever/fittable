package cz.cvut.fit.fittable.shared.authorization.di

import cz.cvut.fit.fittable.shared.authorization.data.AuthorizationLocalDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authorizationModule = module {
    single { AuthorizationLocalDataSource(get()) }
    single(named("api")) {
        provideAuthHttpClient(
            get(),
            get(named("default")),
        )
    }
}
