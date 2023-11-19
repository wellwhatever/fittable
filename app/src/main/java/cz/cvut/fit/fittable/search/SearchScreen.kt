package cz.cvut.fit.fittable.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.R
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.search.domain.model.SearchResult
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    onBackClick: () -> Unit,
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
            searchResults = this.searchResults,
            onSearchResultSelect = onSearchResultSelect,
            onClearQueryClick = viewModel::onClearQueryClick,
            onBackClick = onBackClick
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
    onBackClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChanged,
        onSearch = onSearch,
        active = true,
        trailingIcon = @Composable {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onClearQueryClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ),
                painter = painterResource(id = R.drawable.ic_close_24),
                contentDescription = stringResource(R.string.search_back)
            )
        },
        leadingIcon = @Composable {
            Icon(
                modifier = Modifier
                    .clickable(
                        onClick = onBackClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ),
                painter = painterResource(id = R.drawable.ic_keyboard_backspace_24),
                contentDescription = stringResource(R.string.search_back)
            )
        },
        onActiveChange = {}) {
        SearchResultList(
            modifier = Modifier,
            items = searchResults,
            onSearchResultSelect = onSearchResultSelect
        )
    }
}

@Composable
private fun SearchResultList(
    items: List<SearchResult>,
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = items,
        label = "Search",
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) {
        LazyColumn(
            modifier = modifier,
        ) {
            itemsIndexed(items = it, key = { _, item -> item.id }) { index, item ->
                SearchResultItem(
                    modifier = Modifier.fillMaxWidth(),
                    searchResult = item,
                    onSearchResultSelect = onSearchResultSelect
                )
                if (index != items.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    searchResult: SearchResult,
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable {
                onSearchResultSelect(searchResult.id, searchResult.type)
            }
            .padding(8.dp)
    ) {
        Text(
            text = searchResult.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surfaceTint
        )
        Text(
            text = searchResult.id,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}