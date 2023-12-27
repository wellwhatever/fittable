package cz.cvut.fit.fittable.shared.detail.domain

import cz.cvut.fit.fittable.shared.core.remote.ApiException
import cz.cvut.fit.fittable.shared.detail.converter.TeacherRemoteConverter
import cz.cvut.fit.fittable.shared.detail.domain.model.Teacher
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import kotlin.coroutines.cancellation.CancellationException

class GetTeacherUseCase(
    private val eventsRepository: EventsCacheRepository,
    private val teacherRemoteConverter: TeacherRemoteConverter,
) {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke(username: String): Teacher =
        teacherRemoteConverter.toDomain(eventsRepository.getUser(username))
}
