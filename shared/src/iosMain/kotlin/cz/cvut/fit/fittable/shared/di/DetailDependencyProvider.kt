package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.detail.domain.GetEventByIdUseCase
import cz.cvut.fit.fittable.shared.detail.domain.GetTeacherUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DetailDependencyProvider : KoinComponent {
    val getEventById: GetEventByIdUseCase by inject()
    val getTeacher: GetTeacherUseCase by inject()
}