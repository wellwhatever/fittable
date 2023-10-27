package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import cz.cvut.fit.fittable.shared.core.remote.provideHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single { dataStore(get()) }
    single(named("default")) { provideHttpClient() }
}
