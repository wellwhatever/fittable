package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class GetCoursesEventsUseCase(
    private val eventsRepository: EventsCacheRepository,
    private val eventConverterRemote: EventConverterRemote
) {
    @Throws(CancellationException::class, HttpExceptionDomain::class)
    suspend operator fun invoke(course: String, from: LocalDate, to: LocalDate): List<EventDomain> {
        return eventsRepository.getCoursesEvents(course = course, from = from, to = to).map {
            eventConverterRemote.toDomain(it)
        }
    }
}