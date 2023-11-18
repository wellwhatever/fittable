package cz.cvut.fit.fittable.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.fittable.shared.search.domain.GetSearchResultsUseCase
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

class SearchViewModel(
    private val getSearchResults: GetSearchResultsUseCase
) : ViewModel(), SearchScreenActions {
    private val _searchQuery = MutableStateFlow("")
    private val searchResults = _searchQuery.debounce(0.5.seconds).mapLatest { query ->
        getSearchResults(query)
    }

    val uiState = combine(_searchQuery, searchResults) { query, results ->
        SearchScreenState(
            query = query,
            searchResults = results
        )
    }.stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        SearchScreenState("", emptyList())
    )

    override fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

data class SearchScreenState(
    val query: String,
    val searchResults: List<SearchResult>
)