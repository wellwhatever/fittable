//
//  TimetableViewModel.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 17.12.2023.
//

import Foundation
import shared
import Combine
import Stinsen

extension TimetableScreen{
    @MainActor class ViewModel : ObservableObject{
        let generateHourseUseCase = TimetableDependencyProvider().generateHoursGridUseCase
        let getEventsUseCase = TimetableDependencyProvider().getDayEventsUseCase
        let getFilteredEventsUseCase = TimetableDependencyProvider().getFilteredDayEventsUseCase
        let getCacheEventsUseCase = TimetableDependencyProvider().getCachePersonalEventsUseCase
        let getCachedEventsUseCase = TimetableDependencyProvider().getCachedEventsUseCase
        
        @RouterObject var mainCoordinator : NavigationRouter<MainCoordinator>!
        
        private var cancellables = Set<AnyCancellable>()
        @Published
        var hours: Array<TimetableHour>? = nil
        @Published
        var selectedDate: Kotlinx_datetimeLocalDate = todayDate()
        @Published
        var events: Array<TimetableItem>? = Array()
        @Published
        var search: TimetableSearchResultArgs? = nil
        @Published
        var showSnackBar: Bool = false
        @Published
        var displayNoPermissionError: Bool = false
        
        init(){
            cachePersonalEvents()
            fetchHoursGrid()
            observeSelectedDate()
            observeSearchResult()
            observeFilterData()
        }
        
        private func cachePersonalEvents(){
            Task {
                do {
                    try await getCacheEventsUseCase.invoke()
                } catch{
                    // no-op
                }
            }
        }
        
        private func observeFilterData(){
            $search
                .sink { search in
                    if let category = search?.eventCategory, let id = search?.eventId {
                        self.fetchFilteredEvents(type: category, id: id, date: self.selectedDate)
                    } else {
                        self.fetchEvents(date: self.selectedDate)
                    }
                }
                .store(in: &cancellables)
        }
        
        private func observeSearchResult(){
            mainCoordinator.coordinator.selectedSearchResult
                .compactMap { $0 }
                .removeDuplicates()
                .sink { args in
                    self.search = args
                }
                .store(in: &cancellables)
        }
        
        private func observeSelectedDate(){
            $selectedDate.sink { newDate in
                if let arg = self.search {
                    self.fetchFilteredEvents(type: arg.eventCategory, id: arg.eventId, date: newDate)
                } else {
                    self.fetchEvents(date: newDate)
                }
            }
            .store(in: &cancellables)
        }
        
        private func fetchFilteredEvents(
            type: SearchResultType,
            id: String,
            date: Kotlinx_datetimeLocalDate
        ){
            Task{
                do{
                    events = try await getFilteredEventsUseCase.invoke(type: type, id: id, day: date)
                } catch let error as NSError{
                    if let httpException = error.kotlinException as? HttpExceptionDomain {
                        if(httpException.code == 403){
                            displayNoPermissionError = true
                        }
                    }
                }
            }
        }
        
        private func fetchEvents(date: Kotlinx_datetimeLocalDate? = nil){
            Task{
                do{
                    events = try await getEventsUseCase.invoke(day: date != nil ? date! : todayDate())
                }catch let error as NSError{
                    if let httpException = error.kotlinException as? HttpExceptionDomain {
                        if(httpException.code == 401){
                            mainCoordinator.coordinator.popLast()
                        }
                    }
                    if let noInternet = error.kotlinException as? NoInternetException{
                        showSnackBar.toggle()
                        fetchCachedEvents(date: date)
                    }
                }
            }
        }
        
        private func fetchCachedEvents(date: Kotlinx_datetimeLocalDate? = nil){
            Task {
                do {
                    events = try await getCachedEventsUseCase.invoke(day: date != nil ? date! : todayDate())
                } catch {
                    if let httpException = error as? HttpExceptionDomain {
                        if(httpException.code == 401){
                            mainCoordinator.coordinator.popLast()
                        }
                    }
                }
            }
        }
        
        private func fetchHoursGrid(){
            Task{
                hours = generateHourseUseCase.invoke()
            }
        }
        
        func onDateSelected(date: Date){
            selectedDate = convertToKotlinDateTime(swiftDate: date)
        }
        
        func onEventClick(id: String){
            mainCoordinator.coordinator.routeToEventDetail(id: id)
        }
        
        func onSearchClick(){
            mainCoordinator.coordinator.routeToSearch()
        }
        
        func removeFilter(){
            search = nil
        }
        
        func onContinueClick(){
            displayNoPermissionError = false
            search = nil
            selectedDate = todayDate()
        }
    }
}
