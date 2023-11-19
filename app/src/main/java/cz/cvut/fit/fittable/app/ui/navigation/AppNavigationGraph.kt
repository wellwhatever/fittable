package cz.cvut.fit.fittable.app.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cz.cvut.fit.fittable.authorization.ui.navigation.AUTHORIZATION_NAVIGATION_GRAPH
import cz.cvut.fit.fittable.authorization.ui.navigation.authorizationNavGraph
import cz.cvut.fit.fittable.search.navigation.navigateToSearch
import cz.cvut.fit.fittable.search.navigation.searchNavGraph
import cz.cvut.fit.fittable.timetable.navigation.TIMETABLE_SEARCH_RESULT_ARG
import cz.cvut.fit.fittable.timetable.navigation.TimetableSearchResultArgs
import cz.cvut.fit.fittable.timetable.navigation.navigateToEventDetail
import cz.cvut.fit.fittable.timetable.navigation.navigateToTimetable
import cz.cvut.fit.fittable.timetable.navigation.timetableNavGraph
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalMaterial3Api
@Composable
fun AppNavigationGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = AUTHORIZATION_NAVIGATION_GRAPH,
        modifier = modifier,
    ) {
        authorizationNavGraph(
            onSuccessfulTokenReceive =
            navHostController::navigateToTimetable,
        )
        timetableNavGraph(
            onEventClick = navHostController::navigateToEventDetail,
            onSearchClick = navHostController::navigateToSearch
        )
        searchNavGraph(
            onBackClick = navHostController::popBackStack,
            onSearchResultSelect = { id, type ->
                navHostController.previousBackStackEntry?.savedStateHandle?.set(
                    key = TIMETABLE_SEARCH_RESULT_ARG,
                    value = Json.encodeToString(
                        TimetableSearchResultArgs(
                            type,
                            id
                        )
                    )
                )
                navHostController.popBackStack()
            }
        )
    }
}
