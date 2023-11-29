package cz.cvut.fit.fittable.shared.detail.domain

import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository

class GetEventByIdUseCase(
    private val eventsRepository: EventsCacheRepository
) {
    suspend operator fun invoke(eventId: String): EventDetail =
        with(eventsRepository.getEvent(eventId).event) {
            EventDetail(
                course = links?.course.orEmpty(),
                room = links?.room.orEmpty(),
                sequenceNumber = sequenceNumber.toString(),
                capacity = capacity ?: 0,
                occupied = occupied,
                eventType = eventType,
                parallel = parallel,
                teacherUsernames = links?.teachers.orEmpty()
            )
        }

}