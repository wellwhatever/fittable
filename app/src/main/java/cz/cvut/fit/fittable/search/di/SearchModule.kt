package cz.cvut.fit.fittable.search.di

import cz.cvut.fit.fittable.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val searchModule = module {
    viewModelOf(::SearchViewModel)
}
