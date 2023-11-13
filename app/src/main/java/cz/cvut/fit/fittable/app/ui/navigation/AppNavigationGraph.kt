package cz.cvut.fit.fittable.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cz.cvut.fit.fittable.authorization.ui.navigation.AUTHORIZATION_NAVIGATION_GRAPH
import cz.cvut.fit.fittable.authorization.ui.navigation.authorizationNavGraph
import cz.cvut.fit.fittable.timetable.navigation.navigateToEventDetail
import cz.cvut.fit.fittable.timetable.navigation.navigateToTimetable
import cz.cvut.fit.fittable.timetable.navigation.timetableNavGraph

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
            onEventClick = navHostController::navigateToEventDetail
        )
    }
}
