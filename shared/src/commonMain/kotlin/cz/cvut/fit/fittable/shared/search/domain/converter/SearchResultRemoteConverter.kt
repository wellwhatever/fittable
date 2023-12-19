package cz.cvut.fit.fittable.shared.search.domain.converter

import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultRemote
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult

class SearchResultRemoteConverter {
    fun toDomain(remote: SearchResultRemote) = with(remote) {
        SearchResult(
            id = id,
            title = title,
            type = type
        )
    }
}