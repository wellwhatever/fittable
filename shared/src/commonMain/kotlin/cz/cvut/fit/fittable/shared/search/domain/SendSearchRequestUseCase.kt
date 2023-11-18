package cz.cvut.fit.fittable.shared.search.domain

import cz.cvut.fit.fittable.shared.search.data.SearchRepository
import cz.cvut.fit.fittable.shared.search.domain.converter.SearchResultRemoteConverter
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult

class SendSearchRequestUseCase(
    private val searchRepository: SearchRepository,
    private val searchResultConverter: SearchResultRemoteConverter
) {
    suspend operator fun invoke(query: String) : List<SearchResult> =
        searchRepository.getSearchResults(query).results.map {
            searchResultConverter.toDomain(it)
        }
}