package cz.cvut.fit.fittable.shared.detail.di

import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val detailModule = module {
    singleOf(::GetEventByIdUseCase)
}