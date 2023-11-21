package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetDayEventsGridUseCase internal constructor(
    private val getUserEventsUseCase: GetUserEventsUseCase,
    private val eventConflictMerger: EventConflictMerger,
    private val eventsDayBoundsCalculator: EventsDayBoundsCalculator
) {

    @Throws(CancellationException::class, HttpExceptionDomain::class)
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = eventsDayBoundsCalculator.getRequestDataBounds(day)
        val events = getUserEventsUseCase(from = startDate, to = endDate)

        return eventConflictMerger.mergeConflicts(events = events, day = day)
    }
}