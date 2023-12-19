//
//  MainCoordinator.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 10.12.2023.
//

import Foundation
import Stinsen
import SwiftUI

final class MainCoordinator: NavigationCoordinatable{
    var stack = NavigationStack(initial: \MainCoordinator.authorizationRoute)
    
    @Root var authorizationRoute = makeAuthorization
    @Route(.fullScreen) var timetableRoute = makeTimetable
    @Route(.fullScreen) var eventDetailRoute = makeDetail
    
    init(){
        self.stack = NavigationStack(initial: \MainCoordinator.authorizationRoute)
    }

    func routeToTimetable(){
        self.popToRoot().route(to: \.timetableRoute)
    }
    
    func routeToEventDetail(id : String){
        self.route(to: \.eventDetailRoute, id)
    }
}

extension MainCoordinator{
    @ViewBuilder func makeAuthorization() -> some View {
        AuthorizationScreen()
    }
    @ViewBuilder func makeSearch() -> some View {
        EmptyView()
    }
    @ViewBuilder func makeTimetable() -> some View {
        TimetableScreen()
    }
    @ViewBuilder func makeDetail(id: String) -> some View {
        EventDetailScreen(id: id)
    }
}
