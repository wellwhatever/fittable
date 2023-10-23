package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.di.authorizationModule
import org.koin.dsl.module

val commonModule = module {
    includes(platformModule, authorizationModule)
}
