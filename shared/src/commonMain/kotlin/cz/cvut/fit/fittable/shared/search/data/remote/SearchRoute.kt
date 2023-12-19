package cz.cvut.fit.fittable.shared.search.data.remote

import cz.cvut.fit.fittable.shared.core.remote.NetworkClient
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultsRemote
import io.ktor.http.HttpMethod

internal class SearchRoute(
    private val client: NetworkClient,
) {
    suspend fun getSearchResults(query: String): SearchResultsRemote =
        client.request(
            path = searchRoute,
            method = HttpMethod.Get,
        ) {
            url {
                parameters.append("q", query)
            }
        }

    companion object {
        private const val searchRoute = "search"
    }
}
