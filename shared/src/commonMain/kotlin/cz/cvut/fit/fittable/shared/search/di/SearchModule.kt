package cz.cvut.fit.fittable.shared.search.di

import cz.cvut.fit.fittable.shared.search.data.SearchRepository
import cz.cvut.fit.fittable.shared.search.data.remote.SearchRoute
import cz.cvut.fit.fittable.shared.search.domain.GetFilteredDayEventsUseCase
import cz.cvut.fit.fittable.shared.search.domain.GetSearchResultsUseCase
import cz.cvut.fit.fittable.shared.search.domain.converter.SearchResultRemoteConverter
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val searchModule = module {
    single { SearchRoute(get(named("sirius"))) }

    singleOf(::SearchRepository)

    factoryOf(::GetSearchResultsUseCase)
    factoryOf(::GetFilteredDayEventsUseCase)

    factoryOf(::SearchResultRemoteConverter)
}