package cz.cvut.fit.fittable.shared.search.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SearchResultType {
    @SerialName("course")
    COURSE,

    @SerialName("person")
    PERSON
}