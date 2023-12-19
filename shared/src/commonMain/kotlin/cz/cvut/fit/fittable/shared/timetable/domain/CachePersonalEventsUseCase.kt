package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.days

class CachePersonalEventsUseCase(
    private val eventsRepository: EventsCacheRepository,
    private val eventConverterRemote: EventConverterRemote,
) {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke(): List<EventDomain> {
        val currentDate = Clock.System.now()

        // Cache only events in given cacheRadius
        val from = (currentDate - cacheRadius).toLocalDateTime(TimeZone.currentSystemDefault()).date
        val to = (currentDate + cacheRadius).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return eventsRepository.getPersonalEvents(from = from, to = to).map {
            eventConverterRemote.toDomain(it)
        }
    }

    companion object {
        private val cacheRadius = 7.days
    }
}