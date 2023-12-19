package cz.cvut.fit.fittable.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
            query = query,
            onQueryChanged = viewModel::onQueryChanged,
            searchResults = searchResults,
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
    searchResults: List<SearchResult>,
    onBackClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChanged,
            placeholder = @Composable {
                Text(
                    text = stringResource(id = R.string.search_text_field_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            },
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
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (searchResults.isNotEmpty()) {
            SearchResultList(
                items = searchResults,
                onSearchResultSelect = onSearchResultSelect
            )
        } else {
            SearchResultEmpty(
                modifier = Modifier.padding(top = 16.dp)
            )
        }
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
            items(items = it, key = { item -> item.id }) { item ->
                SearchResultItem(
                    modifier = Modifier.fillMaxWidth(),
                    searchResult = item,
                    onSearchResultSelect = onSearchResultSelect
                )
            }
        }
    }
}

@Composable
private fun SearchResultEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.search_no_results),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_mood_bad_24),
            contentDescription = "No results",
            tint = MaterialTheme.colorScheme.primary
        )
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