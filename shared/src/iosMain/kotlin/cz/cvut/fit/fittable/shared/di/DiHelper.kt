package cz.cvut.fit.fittable.shared.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            commonModule,
            platformModule
        )
    }
}
