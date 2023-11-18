package cz.cvut.fit.fittable.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = getViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    with(state.value) {
        SearchScreenInternal(
            modifier = modifier,
            query = this.query,
            onQueryChanged = viewModel::onQueryChanged,
            onSearch = viewModel::onQueryChanged,
            searchResults = this.searchResults
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun SearchScreenInternal(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<SearchResult>,
    modifier: Modifier = Modifier
) {
    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChanged,
        onSearch = onSearch,
        active = true,
        onActiveChange = {}) {
        SearchResultList(items = searchResults)
    }
}

@Composable
private fun SearchResultList(
    items: List<SearchResult>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = items, key = { item -> item.id }) {
            SearchResultItem(searchResult = it)
        }
    }
}

@Composable
private fun SearchResultItem(
    searchResult: SearchResult,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(searchResult.title)
        Text(searchResult.id)
    }
}