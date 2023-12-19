//
//  TimetableViewModel.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 17.12.2023.
//

import Foundation
import shared
import Combine

extension TimetableScreen{
    @MainActor class ViewModel : ObservableObject{
        let generateHourseUseCase = TimetableDependencyProvider().generateHoursGridUseCase
        let getEventsUseCase = TimetableDependencyProvider().getDayEventsUseCase
        
        private var cancellables = Set<AnyCancellable>()
        @Published
        var hours : Array<TimetableHour>? = nil
        @Published
        var selectedDate : Kotlinx_datetimeLocalDate = todayDate()
        @Published
        var events : Array<TimetableItem>? = Array()
        
        init(){
            fetchHoursGrid()
            observeSelectedDate()
        }
        
        private func fetchEvents(date : Kotlinx_datetimeLocalDate? = nil){
            Task{
                do{
                    events = try await getEventsUseCase.invoke(day: date != nil ? date! : todayDate())
                }catch {
                    if(error is HttpExceptionDomain){
                        print("\((error as! HttpExceptionDomain).code)")
                    }
                    print("Ops!!!")
                }
            }
        }
        
        private func fetchHoursGrid(){
            Task{
                hours = generateHourseUseCase.invoke()
            }
        }
        
        private func observeSelectedDate(){
            $selectedDate.sink{ [weak self] newDate in
                self?.fetchEvents(date: newDate)
            }
            .store(in: &cancellables)
        }
        
        func onDateSelected(date: Date){
            selectedDate = convertToKotlinDateTime(swiftDate: date)
        }
    }
}
