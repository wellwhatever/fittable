package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.search.domain.GetSearchResultsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchDependencyProvider : KoinComponent {
    val searchResultsUseCase: GetSearchResultsUseCase by inject()
}