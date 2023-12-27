package cz.cvut.fit.fittable.shared.search.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultsRemote(
    @SerialName("meta")
    val meta: Meta,
    @SerialName("results")
    val results: List<SearchResultRemote>
)
