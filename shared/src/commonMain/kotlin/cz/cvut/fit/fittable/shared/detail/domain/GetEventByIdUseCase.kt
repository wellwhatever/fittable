package cz.cvut.fit.fittable.shared.detail.domain

import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.data.EventsRepository

class GetEventByIdUseCase(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(eventId: String): EventDetail =
        with(eventsRepository.getEvent(eventId)) {
            EventDetail(
                course = this.links?.course.orEmpty(),
                room = this.links?.room.orEmpty(),
                sequenceNumber = this.sequenceNumber.toString(),
                capacity = this.capacity,
                occupied = this.occupied,
                eventType = this.eventType,
                parallel = this.parallel
            )
        }

}