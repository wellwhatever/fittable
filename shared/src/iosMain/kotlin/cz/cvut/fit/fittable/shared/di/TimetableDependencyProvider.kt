package cz.cvut.fit.fittable.shared.di

import cz.cvut.fit.fittable.shared.search.domain.GetFilteredDayEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.CachePersonalEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.EventConflictMerger
import cz.cvut.fit.fittable.shared.timetable.domain.EventsDayBoundsCalculator
import cz.cvut.fit.fittable.shared.timetable.domain.GenerateHoursGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetCachedEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetPersonalDayEventsGridUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetTimetableCalendarBoundsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

object TimetableDependencyProvider : KoinComponent {
    val generateHoursGridUseCase: GenerateHoursGridUseCase by inject()
    val getDayEventsUseCase : GetPersonalDayEventsGridUseCase by inject()
//    fun getFilteredDayEventsUseCase() = get<GetFilteredDayEventsUseCase>()
//    fun getCachePersonalEventsUseCase() = get<CachePersonalEventsUseCase>()
//    fun getCachedEventsUseCase() = get<GetCachedEventsUseCase>()
//    fun getCalendarBoundsUseCase() = get<GetTimetableCalendarBoundsUseCase>()
}