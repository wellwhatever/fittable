package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetPersonalDayEventsGridUseCase internal constructor(
    private val eventsRepository: EventsCacheRepository,
    private val eventConflictMerger: EventConflictMerger,
    private val eventsDayBoundsCalculator: EventsDayBoundsCalculator,
    private val eventConverterRemote: EventConverterRemote
) {
    @Throws(CancellationException::class, HttpExceptionDomain::class)
    suspend operator fun invoke(day: LocalDate): List<TimetableItem> {
        val (startDate, endDate) = eventsDayBoundsCalculator.getRequestDataBounds(day)
        val events = eventsRepository.getPersonalEvents(from = startDate, to = endDate).map {
            eventConverterRemote.toDomain(it)
        }

        return eventConflictMerger.mergeConflicts(events = events, day = day)
    }
}