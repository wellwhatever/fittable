package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetDayEventsGridUseCase internal constructor(
    private val getUserEventsUseCase: GetUserEventsUseCase,
    private val eventConflictMerger: EventConflictMerger,
    private val eventsDayBoundsCalculator: EventsDayBoundsCalculator,
    private val usernameLocalDataSource: UsernameLocalDataSource,
) {

    @Throws(CancellationException::class, HttpExceptionDomain::class)
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = eventsDayBoundsCalculator.getRequestDataBounds(day)
        val username = usernameLocalDataSource.usernameFlow.first()
        val events = getUserEventsUseCase(username = username, from = startDate, to = endDate)

        return eventConflictMerger.mergeConflicts(events = events, day = day)
    }
}