package cz.cvut.fit.fittable.detail.di

import cz.cvut.fit.fittable.detail.EventDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val eventDetailModule = module {
    viewModelOf(::EventDetailViewModel)
}