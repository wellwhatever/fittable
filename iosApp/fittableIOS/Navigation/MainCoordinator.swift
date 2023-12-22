//
//  MainCoordinator.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 10.12.2023.
//

import Foundation
import Stinsen
import SwiftUI
import shared
import Combine

final class MainCoordinator: NavigationCoordinatable{
    var stack = NavigationStack(initial: \MainCoordinator.authorizationRoute)
    
    var selectedSearchResult = CurrentValueSubject<TimetableSearchResultArgs?, Never>(nil)
    
    @Root var authorizationRoute = makeAuthorization
    @Route(.fullScreen) var timetableRoute = makeTimetable
    @Route(.fullScreen) var eventDetailRoute = makeDetail
    @Route(.push) var searchScreen = makeSearch
    
    init(){
        self.stack = NavigationStack(initial: \MainCoordinator.authorizationRoute)
    }

    func routeToTimetable(){
        self.popToRoot().route(to: \.timetableRoute)
    }
    
    func routeToEventDetail(id : String){
        self.route(to: \.eventDetailRoute, id)
    }
    
    func routeToSearch(){
        self.route(to: \.searchScreen)
    }
    
    func onSearchResultPicked(result: TimetableSearchResultArgs){
        selectedSearchResult.send(result)
        self.popLast()
    }
}

extension MainCoordinator{
    @ViewBuilder func makeAuthorization() -> some View {
        AuthorizationScreen()
    }
    @ViewBuilder func makeSearch() -> some View {
        SearchScreen()
    }
    @ViewBuilder func makeTimetable() -> some View {
        TimetableScreen()
    }
    @ViewBuilder func makeDetail(id: String) -> some View {
        EventDetailScreen(id: id)
    }
}
