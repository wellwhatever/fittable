package cz.cvut.fit.fittable.shared.search.domain.model

import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType

data class SearchResult(
    val id: String,
    val title: String,
    val type: SearchResultType
)
