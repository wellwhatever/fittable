package cz.cvut.fit.fittable.shared.timetable.di

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.timetable.data.EventsRepository
import cz.cvut.fit.fittable.shared.timetable.domain.EventConflictCalculator
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetTimetableHeaderUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetUserEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterDomain
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterRemote
import cz.cvut.fit.fittable.shared.timetable.data.remote.EventsRoute
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val timetableModule = module {
    single {
        NetworkClient(
            baseUrl = "https://sirius.fit.cvut.cz/api/v1/",
            httpClient = get(named("api")),
        )
    }

    singleOf(::EventsRepository)
    singleOf(::EventsRoute)

    factoryOf(::GenerateHoursGridUseCase)
    factoryOf(::GetUserEventsUseCase)
    factoryOf(::GetDayEventsGridUseCase)
    factoryOf(::GetTimetableHeaderUseCase)

    factoryOf(::EventsConverterRemote)
    factoryOf(::EventsConverterDomain)
    factoryOf(::EventConflictCalculator)
}
