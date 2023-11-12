package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.data.TimetableRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class GetUserEventsUseCase(
    private val timetableRepository: TimetableRepository,
    private val eventsConverterRemote: EventsConverterRemote
) {
    suspend operator fun invoke(from: LocalDate, to: LocalDate): List<TimetableEvent> {
        val events = timetableRepository.getUserEvents(from, to)
        return eventsConverterRemote.toDomain(events)
    }

    private fun generateFakeEvents(): List<TimetableEvent> {
        // TODO faked for now, remove after testing
        val now: Instant = Clock.System.now()
        val event1start = now - 15.hours
        val event1end = event1start + 1.hours + 30.minutes
        val duration1 = event1end - event1start

        val event2start = event1end
        val event2end = event2start + 1.hours + 30.minutes
        val event2duration = event2end - event2start

        val event3start = event2end
        val event3end = event3start + 1.hours + 30.minutes
        val event3duration = event3end - event3start

        return listOf(
            TimetableEvent(
                title = "Bi-KSP",
                room = "T9-301",
                start = event1start,
                end = event1end,
                duration = duration1,
                id = "1",
            ),
            TimetableEvent(
                title = "Bi-AMP",
                room = "T9-202",
                start = event2start,
                end = event2end,
                duration = event2duration,
                id = "2",
            ),
            TimetableEvent(
                title = "Bi-AMP",
                room = "T9-202",
                start = event3start,
                end = event3end,
                duration = event3duration,
                id = "3",
            )
        )
    }
}