//
//  SearchViewModel.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 20.12.2023.
//

import Foundation
import shared
import Combine
import Stinsen

extension SearchScreen{
    @MainActor class ViewModel : ObservableObject {
        @RouterObject var mainCoordinator : NavigationRouter<MainCoordinator>!
        
        let getSearchResultsUseCase = SearchDependencyProvider().searchResultsUseCase
        
        private var cancellables = Set<AnyCancellable>()
        @Published var searchResults: Array<SearchResult> = []
        @Published var query : String = ""
        
        init() {
            observeQueryChange()
        }
        
        func observeQueryChange(){
            $query
                .debounce(for: .seconds(0.5), scheduler: DispatchQueue.main)
                .sink { newQuery in
                    self.fetchSearchResults(query: newQuery)
                }
                .store(in: &cancellables)
        }
        
        func fetchSearchResults(query: String){
            Task {
                do{
                    searchResults = try await getSearchResultsUseCase.invoke(query: query)
                } catch {
                    // no-op, try again
                }
            }
        }
        
        func onSearchResultClick(id: String, name: String){
            let resultType: SearchResultType? = SearchResultType.allCases.first { item in
                item.name == name
            } ?? nil
            
            if(resultType != nil){
                mainCoordinator
                    .coordinator
                    .onSearchResultPicked(result: TimetableSearchResultArgs(eventCategory: resultType!, eventId: id))
            }
        }
        
    }
}
