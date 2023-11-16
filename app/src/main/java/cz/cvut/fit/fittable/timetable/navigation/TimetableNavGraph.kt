package cz.cvut.fit.fittable.timetable.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import cz.cvut.fit.fittable.detail.EventDetailScreen
import cz.cvut.fit.fittable.timetable.ui.TimetableScreen
import java.net.URLDecoder
import java.net.URLEncoder

internal const val TIMETABLE_NAVIGATION_GRAPH = "timetable_nav_graph"
internal const val TIMETABLE_ROUTE = "timetable_route"

internal const val EVENT_DETAIL_ROUTE = "event_detail_route"
internal const val EVENT_ID_ARG = "eventId"
private val urlCharacterEncoding = Charsets.UTF_8.name()

internal class EventDetailArgs(val eventId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[EVENT_ID_ARG]),
                    urlCharacterEncoding
                )
            )
}


fun NavController.navigateToTimetable() {
    this.navigate(TIMETABLE_ROUTE)
}

fun NavController.navigateToEventDetail(eventId: String) {
    val encodedId = URLEncoder.encode(eventId, urlCharacterEncoding)
    this.navigate("$EVENT_DETAIL_ROUTE/$encodedId") {
        launchSingleTop = true
    }
}

internal fun NavGraphBuilder.timetableNavGraph(
    navController : NavController,
    onEventClick: (eventId: String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = TIMETABLE_NAVIGATION_GRAPH,
        startDestination = TIMETABLE_ROUTE,
    ) {
        composable(
            route = TIMETABLE_ROUTE,
        ) {
            TimetableScreen(navController)
        }
        composable(
            route = "$EVENT_DETAIL_ROUTE/{$EVENT_ID_ARG}",
            arguments = listOf(
                navArgument(EVENT_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            EventDetailScreen()
        }
        nestedGraphs()
    }
}
