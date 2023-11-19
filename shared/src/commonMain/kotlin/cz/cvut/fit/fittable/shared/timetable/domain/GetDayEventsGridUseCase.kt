package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.datetime.LocalDate

class GetDayEventsGridUseCase internal constructor(
    private val getUserEventsUseCase: GetUserEventsUseCase,
    private val eventConflictMerger: EventConflictMerger,
    private val eventsDayBoundsCalculator: EventsDayBoundsCalculator
) {
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = eventsDayBoundsCalculator.getRequestDataBounds(day)
        val events = getUserEventsUseCase(from = startDate, to = endDate)

        return eventConflictMerger.mergeConflicts(events = events, day = day)
    }
}