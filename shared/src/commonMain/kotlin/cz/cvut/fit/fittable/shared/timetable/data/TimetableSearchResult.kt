package cz.cvut.fit.fittable.shared.timetable.data

import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TimetableSearchResultArgs(
    @SerialName("event_category")
    val eventCategory: SearchResultType,
    @SerialName("event_id")
    val eventId: String,
)