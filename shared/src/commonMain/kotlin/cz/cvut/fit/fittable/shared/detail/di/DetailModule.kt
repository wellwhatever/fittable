package cz.cvut.fit.fittable.shared.detail.di

import cz.cvut.fit.fittable.shared.detail.converter.TeacherRemoteConverter
import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import cz.cvut.fit.fittable.shared.detail.domain.GetTeacherUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val detailModule = module {
    factoryOf(::GetEventByIdUseCase)
    factoryOf(::GetTeacherUseCase)
    factoryOf(::TeacherRemoteConverter)
}