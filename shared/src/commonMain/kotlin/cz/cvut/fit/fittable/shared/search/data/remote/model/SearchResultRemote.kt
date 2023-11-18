package cz.cvut.fit.fittable.shared.search.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultRemote(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
)