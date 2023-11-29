package cz.cvut.fit.fittable.timetable.navigation

import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import cz.cvut.fit.fittable.detail.EventDetailScreen
import cz.cvut.fit.fittable.route.ui.TimetableRoute
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

internal const val TIMETABLE_NAVIGATION_GRAPH = "timetable_nav_graph"
internal const val TIMETABLE_ROUTE = "timetable_route"
internal const val TIMETABLE_SEARCH_RESULT_ARG = "search_result_id"

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

@Serializable
class TimetableSearchResultArgs(
    @SerialName("event_category")
    val eventCategory: SearchResultType,
    @SerialName("event_id")
    val eventId: String,
)

class TimetableArgs(
    val searchResult: TimetableSearchResultArgs
) {
    constructor(raw: String) : this(
        Json.decodeFromString<TimetableSearchResultArgs>(raw)
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
    onEventClick: (eventId: String) -> Unit,
    onSearchClick: () -> Unit,
    navigateToAuthorization: () -> Unit,
    onBack: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = TIMETABLE_NAVIGATION_GRAPH,
        startDestination = TIMETABLE_ROUTE,
    ) {
        composable(
            route = "$TIMETABLE_ROUTE?$TIMETABLE_SEARCH_RESULT_ARG={$TIMETABLE_SEARCH_RESULT_ARG}",
            arguments = listOf(
                navArgument(TIMETABLE_SEARCH_RESULT_ARG) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            // TODO remove after bug with saved state handle will be resolved!!!
            val searchResultFlow: State<String?> = it
                .savedStateHandle
                .getStateFlow(TIMETABLE_SEARCH_RESULT_ARG, null).collectAsStateWithLifecycle()

            val args = searchResultFlow.value
            TimetableRoute(
                onEventClick = onEventClick,
                onSearchClick = onSearchClick,
                navigateToAuthorization = navigateToAuthorization,
                searchArgs = if (args != null) {
                    it.savedStateHandle.remove<String>(TIMETABLE_SEARCH_RESULT_ARG)
                    TimetableArgs(args)
                } else {
                    null
                }
            )
        }
        composable(
            route = "$EVENT_DETAIL_ROUTE/{$EVENT_ID_ARG}",
            arguments = listOf(
                navArgument(EVENT_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            EventDetailScreen(onBack = onBack)
        }
        nestedGraphs()
    }
}
