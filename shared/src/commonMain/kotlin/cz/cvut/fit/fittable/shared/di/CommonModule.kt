package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.di.authorizationModule
import cz.cvut.fit.fittable.shared.detail.di.detailModule
import cz.cvut.fit.fittable.shared.timetable.di.timetableModule
import org.koin.dsl.module

val commonModule = module {
    includes(platformModule, authorizationModule, timetableModule, detailModule)
}
