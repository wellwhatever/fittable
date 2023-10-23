package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { dataStore(get()) }
}
