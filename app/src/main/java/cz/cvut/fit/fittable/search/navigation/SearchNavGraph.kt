package cz.cvut.fit.fittable.search.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cz.cvut.fit.fittable.search.SearchScreen
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType

internal const val SEARCH_NAVIGATION_GRAPH = "search_nav_graph"
internal const val SEARCH_ROUTE = "search_route"

fun NavController.navigateToSearch() {
    this.navigate(SEARCH_ROUTE)
}

@ExperimentalMaterial3Api
internal fun NavGraphBuilder.searchNavGraph(
    onBackClick: () -> Unit,
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
) {
    navigation(
        route = SEARCH_NAVIGATION_GRAPH,
        startDestination = SEARCH_ROUTE,
    ) {
        composable(
            route = SEARCH_ROUTE,
        ) {
            SearchScreen(
                onBackClick = onBackClick,
                onSearchResultSelect = onSearchResultSelect
            )
        }
    }
}
