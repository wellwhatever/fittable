package cz.cvut.fit.fittable.search.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cz.cvut.fit.fittable.search.SearchScreen
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.timetable.navigation.TIMETABLE_NAVIGATION_GRAPH
import cz.cvut.fit.fittable.timetable.navigation.TIMETABLE_ROUTE

internal const val SEARCH_NAVIGATION_GRAPH = "search_nav_graph"
internal const val SEARCH_ROUTE = "search_route"

fun NavController.navigateToSearch() {
    this.navigate(TIMETABLE_ROUTE)
}

@ExperimentalMaterial3Api
internal fun NavGraphBuilder.searchNavGraph(
    onSearchResultSelect: (id: String, type: SearchResultType) -> Unit,
) {
    navigation(
        route = TIMETABLE_NAVIGATION_GRAPH,
        startDestination = TIMETABLE_ROUTE,
    ) {
        composable(
            route = TIMETABLE_ROUTE,
        ) {
            SearchScreen(onSearchResultSelect = onSearchResultSelect)
        }
    }
}