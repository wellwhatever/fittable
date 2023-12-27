package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.search.domain.GetFilteredDayEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.CachePersonalEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetCachedEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetPersonalDayEventsGridUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object TimetableDependencyProvider : KoinComponent {
    val generateHoursGridUseCase: GenerateHoursGridUseCase by inject()
    val getDayEventsUseCase: GetPersonalDayEventsGridUseCase by inject()
    val getFilteredDayEventsUseCase: GetFilteredDayEventsUseCase by inject()
    val getCachePersonalEventsUseCase: CachePersonalEventsUseCase by inject()
    val getCachedEventsUseCase: GetCachedEventsUseCase by inject()
}
