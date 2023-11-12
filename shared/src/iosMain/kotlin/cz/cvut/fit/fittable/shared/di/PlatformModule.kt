package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import cz.cvut.fit.fittable.shared.core.remote.provideHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single { dataStore() }
    single(named("default")) { provideHttpClient() }
}
