package cz.cvut.fit.fittable.timetable.di

import cz.cvut.fit.fittable.timetable.ui.TimetableViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val timetableModule = module {
    viewModelOf(::TimetableViewModel)
}
