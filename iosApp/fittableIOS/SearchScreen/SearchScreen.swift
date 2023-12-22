//
//  SearchScreen.swift
//  fittableIOS
//
//  Created by Oleksandr Petrov on 20.12.2023.
//

import SwiftUI
import shared
import Stinsen

struct SearchScreen: View {
    @StateObject var viewModel = ViewModel()
    let colorScheme = TokenColor()
    
    var body: some View {
        SearchScreenSearchBar(searchResults: viewModel.searchResults)
    }
    
    @ViewBuilder func SearchScreenSearchBar(searchResults: Array<SearchResult>) -> some View {
        NavigationStack {
            SearchScreenInternal(searchResults: searchResults)
        }
        .searchable(text: $viewModel.query)
    }
    
    @ViewBuilder func SearchScreenInternal(searchResults: Array<SearchResult>) -> some View {
        if(!searchResults.isEmpty){
            SearchResultsList(items: searchResults, onSearchResultSelect: viewModel.onSearchResultClick)
        } else {
            SearchResultEmpty()
        }
    }
    
    @ViewBuilder
    func SearchResultsList(items: [SearchResult], onSearchResultSelect: @escaping (String, String) -> Void) -> some View {
        List(items, id: \.id) { item in
            SearchResultItem(searchResult: item, onSearchResultSelect: onSearchResultSelect)
        }
    }
    
    @ViewBuilder
    func SearchResultItem(searchResult: SearchResult, onSearchResultSelect: @escaping (String, String) -> Void) -> some View {
        HStack(spacing: 0){
            VStack(alignment: .leading, spacing: 8){
                Text(searchResult.title)
                    .foregroundColor(colorScheme.surfaceTint)
                    .font(.body)
                
                Text(searchResult.id)
                    .foregroundColor(colorScheme.outline)
                    .font(.subheadline)
            }
            
            Spacer()
        }
        .onTapGesture {
            onSearchResultSelect(searchResult.id, searchResult.type.name)
        }
    }
    
    @ViewBuilder
    func SearchResultEmpty() -> some View {
        VStack(spacing: 16) {
            Text("No results")
                .multilineTextAlignment(.center)
                .font(.headline)
                .foregroundColor(Color.primary)
        }
        .frame(maxWidth: .infinity)
        .padding()
    }
}
