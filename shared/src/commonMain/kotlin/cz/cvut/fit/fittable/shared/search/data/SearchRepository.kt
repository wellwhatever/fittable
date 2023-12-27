package cz.cvut.fit.fittable.shared.search.data

import cz.cvut.fit.fittable.shared.search.data.remote.SearchRoute

class SearchRepository internal constructor(
    private val searchRoute: SearchRoute,
) {
    suspend fun getSearchResults(query: String) = searchRoute.getSearchResults(query)
}
