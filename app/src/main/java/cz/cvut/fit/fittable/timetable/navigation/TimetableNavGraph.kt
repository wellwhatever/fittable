package cz.cvut.fit.fittable.timetable.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cz.cvut.fit.fittable.timetable.ui.TimetableScreen

const val TIMETABLE_NAVIGATION_GRAPH = "timetable_nav_graph"
const val TIMETABLE_ROUTE = "timetable_route"
fun NavController.navigateToTimetable() {
    this.navigate(TIMETABLE_ROUTE)
}

fun NavGraphBuilder.timetableNavGraph(
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = TIMETABLE_NAVIGATION_GRAPH,
        startDestination = TIMETABLE_ROUTE,
    ) {
        composable(
            route = TIMETABLE_ROUTE,
        ) {
            TimetableScreen()
        }
        nestedGraphs()
    }
}
