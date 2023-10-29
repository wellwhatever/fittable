package cz.cvut.fit.fittable.shared.timetable.di

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.timetable.data.TimetableRepository
import cz.cvut.fit.fittable.shared.timetable.remote.EventsRoute
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val timetableModule = module {
    single {
        NetworkClient(
            baseUrl = "https://sirius.fit.cvut.cz/api/v1",
            httpClient = get(named("api")),
        )
    }
    singleOf(::TimetableRepository)
    singleOf(::EventsRoute)
}
