//
//  EventDetailViewModel.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 19.12.2023.
//

import SwiftUI
import Stinsen
import shared

extension EventDetailScreen{
    @MainActor class ViewModel: ObservableObject{
        let id: String
        @RouterObject var mainCoordinator : NavigationRouter<MainCoordinator>!
        
        let getEventByIdUseCase = DetailDependencyProvider().getEventById
        
        @Published
        var event: EventDetail_? = nil
        
        init(id: String){
            self.id = id
            fetchEventDetails()
        }
        
        
        func fetchEventDetails(){
            Task{
                do{
                    event = try await getEventByIdUseCase.invoke(eventId: id)
                } catch {
                    // Handle error here
                }
            }
        }
        
        func onArrowBackClick(){
            mainCoordinator.popLast()
        }
    }
}
