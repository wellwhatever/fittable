package cz.cvut.fit.fittable.shared.timetable.di

import cz.cvut.fit.fittable.TimetableDatabase
import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.sqldelight.createDatabase
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.data.local.EventsLocalDataSource
import cz.cvut.fit.fittable.shared.timetable.data.local.converter.EventConverterLocal
import cz.cvut.fit.fittable.shared.timetable.data.remote.EventsRoute
import cz.cvut.fit.fittable.shared.timetable.domain.CachePersonalEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.EventConflictMerger
import cz.cvut.fit.fittable.shared.timetable.domain.EventsDayBoundsCalculator
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetCachedEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetCoursesEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetPersonalDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetRoomEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetTimetableCalendarBoundsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetUserEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterDomain
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val timetableModule = module {
    single(named("sirius")) {
        NetworkClient(
            baseUrl = "https://sirius.fit.cvut.cz/api/v1/",
            httpClient = get(named("api")),
        )
    }
    single { EventsRoute(get(named("sirius"))) }

    single {
        createDatabase(get())
    }

    single {
        get<TimetableDatabase>().timetableDatabaseQueries
    }

    single {
        EventsLocalDataSource(
            get(),
            get(),
            get(named("IoDispatcher"))
        )
    }

    singleOf(::EventsCacheRepository)

    factoryOf(::GenerateHoursGridUseCase)
    factoryOf(::GetUserEventsUseCase)
    factoryOf(::GetPersonalDayEventsGridUseCase)
    factoryOf(::GetTimetableCalendarBoundsUseCase)
    factoryOf(::GetRoomEventsUseCase)
    factoryOf(::GetCoursesEventsUseCase)
    factoryOf(::CachePersonalEventsUseCase)
    factoryOf(::GetCachedEventsUseCase)

    factoryOf(::EventConverterLocal)
    factoryOf(::EventConverterRemote)
    factoryOf(::EventConverterDomain)
    factoryOf(::EventConflictMerger)
    factoryOf(::EventsDayBoundsCalculator)
    factoryOf(::EventConflictMerger)
}
