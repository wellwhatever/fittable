package cz.cvut.fit.fittable.shared.core.remote

import org.koin.dsl.module

internal val remoteModule = module {
    single { provideHttpClient() }
}
