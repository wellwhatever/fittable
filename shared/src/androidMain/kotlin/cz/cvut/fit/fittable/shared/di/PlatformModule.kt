package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import cz.cvut.fit.fittable.shared.authorization.data.local.authorizationDataStoreFileName
import cz.cvut.fit.fittable.shared.authorization.data.local.usernameDataStoreFileName
import cz.cvut.fit.fittable.shared.authorization.domain.SaveAuthorizationTokenUseCase
import cz.cvut.fit.fittable.shared.core.remote.provideHttpClient
import cz.cvut.fit.fittable.shared.sqldelight.DriverFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single(named("authorization")) { dataStore(get(), authorizationDataStoreFileName) }
    single(named("username")) { dataStore(get(), usernameDataStoreFileName) }
    single(named("default")) { provideHttpClient() }

    singleOf(::DriverFactory)
    factoryOf(::SaveAuthorizationTokenUseCase)
}
