package cz.cvut.fit.fittable.shared.search.domain

import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.search.data.SearchRepository
import cz.cvut.fit.fittable.shared.search.domain.converter.SearchResultRemoteConverter
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult
import kotlin.coroutines.cancellation.CancellationException

class GetSearchResultsUseCase(
    private val searchRepository: SearchRepository,
    private val searchResultConverter: SearchResultRemoteConverter
) {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke(query: String): List<SearchResult> =
        searchRepository.getSearchResults(query).results.map {
            searchResultConverter.toDomain(it)
        }
}
