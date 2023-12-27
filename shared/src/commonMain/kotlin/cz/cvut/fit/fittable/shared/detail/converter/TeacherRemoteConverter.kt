package cz.cvut.fit.fittable.shared.detail.converter

import cz.cvut.fit.fittable.shared.detail.domain.model.Teacher
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.User

class TeacherRemoteConverter {
    fun toDomain(remote: User) = with(remote.people) {
        Teacher(
            id = id,
            fullName = fullName
        )
    }
}
