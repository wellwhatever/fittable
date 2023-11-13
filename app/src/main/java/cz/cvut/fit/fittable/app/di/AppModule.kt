package cz.cvut.fit.fittable.app.di

import cz.cvut.fit.fittable.authorization.di.authorizationModule
import cz.cvut.fit.fittable.detail.di.eventDetailModule
import cz.cvut.fit.fittable.shared.di.commonModule
import cz.cvut.fit.fittable.timetable.di.timetableModule
import org.koin.dsl.module

val appModule
    get() = module {
        includes(
            commonModule,
            androidModule,
            authorizationModule,
            timetableModule,
            eventDetailModule
        )
    }

private val androidModule = module {
}
