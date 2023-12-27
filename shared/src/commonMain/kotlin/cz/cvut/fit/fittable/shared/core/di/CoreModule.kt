package cz.cvut.fit.fittable.shared.core.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val coreModule = module {
    single(named("IoDispatcher")) {
        Dispatchers.IO
    }
    single(named("DefaultDispatcher")) {
        Dispatchers.Default
    }
    single(named("MainDispatcher")) {
        Dispatchers.Main
    }
}
