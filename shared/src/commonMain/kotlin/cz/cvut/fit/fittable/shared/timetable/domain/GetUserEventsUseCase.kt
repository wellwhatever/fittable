package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.core.remote.HttpExceptionDomain
import cz.cvut.fit.fittable.shared.timetable.data.EventsRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class GetUserEventsUseCase(
    private val eventsRepository: EventsRepository,
    private val eventsConverterRemote: EventsConverterRemote,
    private val usernameLocalDataSource: UsernameLocalDataSource
) {
    @Throws(CancellationException::class, HttpExceptionDomain::class)
    suspend operator fun invoke(from: LocalDate, to: LocalDate): List<EventDomain> {
        val username = usernameLocalDataSource.usernameFlow.first()
        val events = eventsRepository.getUserEvents(from = from, to = to, username = username)
        return eventsConverterRemote.toDomain(events)
    }

    private fun generateFakeEvents(): List<EventDomain> {
        // TODO faked for now, remove after testing
        val now: Instant = Clock.System.now()
        val event1start = now - 8.hours
        val event1end = event1start + 1.hours + 30.minutes
        val duration1 = event1end - event1start

        val event2start = event1end - 1.hours
        val event2end = event2start + 1.hours + 30.minutes
        val event2duration = event2end - event2start

        val event3start = event2end
        val event3end = event3start + 1.hours + 30.minutes
        val event3duration = event3end - event3start

        return listOf(
            EventDomain(
                title = "Bi-KSP", room = "T9-301", start = event1start, end = event1end, id = "1"
            ), EventDomain(
                title = "Bi-AMP", room = "T9-202", start = event2start, end = event2end, id = "2"
            ), EventDomain(
                title = "Bi-AMP", room = "T9-202", start = event3start, end = event3end, id = "3"
            )
        )
    }
}