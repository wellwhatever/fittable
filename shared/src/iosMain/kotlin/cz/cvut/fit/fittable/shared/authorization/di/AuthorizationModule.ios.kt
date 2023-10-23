package cz.cvut.fit.fittable.shared.authorization.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import org.koin.dsl.module

val authorizationModule = module {
    single { dataStore() }
}
