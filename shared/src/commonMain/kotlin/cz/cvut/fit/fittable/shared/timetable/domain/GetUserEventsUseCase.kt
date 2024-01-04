package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetUserEventsUseCase(
    private val eventsRepository: EventsCacheRepository,
    private val eventConverterRemote: EventConverterRemote,
) {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke(
        username: String,
        from: LocalDate,
        to: LocalDate
    ): List<EventDomain> {
        val events = eventsRepository.getUserEvents(from = from, to = to, username = username)
        return events.map { eventConverterRemote.toDomain(it) }
    }
}
