package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.data.EventsRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventsConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import kotlinx.datetime.LocalDate

class GetRoomEventsUseCase(
    private val eventsRepository: EventsRepository,
    private val eventsConverterRemote: EventsConverterRemote
) {
    suspend operator fun invoke(room: String, from: LocalDate, to: LocalDate): List<EventDomain> {
        val events = eventsRepository.getRoomEvents(room = room, from = from, to = to)
        return eventsConverterRemote.toDomain(events)
    }
}