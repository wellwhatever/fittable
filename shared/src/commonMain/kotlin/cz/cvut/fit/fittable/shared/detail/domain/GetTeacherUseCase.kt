package cz.cvut.fit.fittable.shared.detail.domain

import cz.cvut.fit.fittable.shared.detail.converter.TeacherRemoteConverter
import cz.cvut.fit.fittable.shared.detail.domain.model.Teacher
import cz.cvut.fit.fittable.shared.timetable.data.EventsRepository

class GetTeacherUseCase(
    private val eventsRepository: EventsRepository,
    private val teacherRemoteConverter: TeacherRemoteConverter,
) {
    suspend operator fun invoke(username: String): Teacher =
        teacherRemoteConverter.toDomain(eventsRepository.getUser(username))
}