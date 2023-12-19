package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.authorization.domain.SaveAuthorizationTokenUseCase
import cz.cvut.fit.fittable.shared.authorization.domain.SaveUsernameUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetPersonalDayEventsGridUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

object AuthorizationDependencyProvider : KoinComponent {
    val saveAuthorizationTokenUseCase: SaveAuthorizationTokenUseCase by inject()
}