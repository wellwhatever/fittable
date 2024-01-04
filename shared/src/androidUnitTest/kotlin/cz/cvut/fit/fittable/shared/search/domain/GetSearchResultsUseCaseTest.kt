package cz.cvut.fit.fittable.shared.search.domain


import cz.cvut.fit.fittable.shared.search.data.SearchRepository
import cz.cvut.fit.fittable.shared.search.data.remote.model.Meta
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultRemote
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultsRemote
import cz.cvut.fit.fittable.shared.search.domain.converter.SearchResultRemoteConverter
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

class GetSearchResultsUseCaseTest : FunSpec({

    test("returns list of SearchResults from repository") {
        // Mock dependencies
        val searchRepository = mockk<SearchRepository>()
        val searchResultConverter = mockk<SearchResultRemoteConverter>()

        // Create an instance of the use case
        val getSearchResultsUseCase =
            GetSearchResultsUseCase(searchRepository, searchResultConverter)

        // Define test data
        val query = "Pa1"

        val fakeRemoteResult = SearchResultsRemote(
            meta = Meta(
                count = 1,
                limit = 1,
                offset = 0
            ),
            results = listOf(
                SearchResultRemote(
                    id = "123",
                    title = "title",
                    type = SearchResultType.COURSE
                )
            )
        )
        val fakeDomainResult = SearchResult(
            id = "123",
            title = "title",
            type = SearchResultType.COURSE
        )

        // Stub searchRepository to return fake remote results
        coEvery {
            searchRepository.getSearchResults(query)
        } returns fakeRemoteResult

        // Stub searchResultConverter to convert fake remote results to domain results
        every {
            searchResultConverter.toDomain(any())
        } returns fakeDomainResult

        // Call the use case
        val result = getSearchResultsUseCase(query)

        // Verify that the result is the list of domain results
        result shouldBe listOf(fakeDomainResult)
    }
})