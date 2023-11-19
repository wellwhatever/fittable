package cz.cvut.fit.fittable.shared.search.domain

import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.timetable.domain.EventConflictMerger
import cz.cvut.fit.fittable.shared.timetable.domain.EventsDayBoundsCalculator
import cz.cvut.fit.fittable.shared.timetable.domain.GetCoursesEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetRoomEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetUserEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.datetime.LocalDate

class GetFilteredDayEventsUseCase internal constructor(
    private val getCoursesEvents: GetCoursesEventsUseCase,
    private val getRoomEvents: GetRoomEventsUseCase,
    private val getUserEvents: GetUserEventsUseCase,
    private val eventsDayBoundsCalculator: EventsDayBoundsCalculator,
    private val eventsMerger: EventConflictMerger
) {
    suspend operator fun invoke(
        type: SearchResultType,
        id: String,
        day: LocalDate,
    ): List<TimetableItem> {
        val (from, to) = eventsDayBoundsCalculator.getRequestDataBounds(day)
        val events = when (type) {
            SearchResultType.COURSE -> getCoursesEvents(course = id, from = from, to = to)

            SearchResultType.PERSON -> getUserEvents(from = from, to = to)

            SearchResultType.ROOM -> getRoomEvents(room = id, from = from, to = to)
        }

        return eventsMerger.mergeConflicts(events, day)
    }
}