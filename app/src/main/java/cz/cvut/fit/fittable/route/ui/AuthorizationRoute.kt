package cz.cvut.fit.fittable.route.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.core.ui.Loading
import cz.cvut.fit.fittable.timetable.navigation.TimetableArgs
import cz.cvut.fit.fittable.timetable.ui.TimetableScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun TimetableRoute(
    searchArgs: TimetableArgs?,
    onSearchClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit,
    navigateToAuthorization: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthorizationRouteViewModel = getViewModel(),
) {
    val authorization by viewModel.uiState.collectAsStateWithLifecycle()

    when (authorization) {
        is AuthorizationRouteUiState.Loading -> Loading()
        is AuthorizationRouteUiState.Authorized -> TimetableScreen(
            searchResult = searchArgs,
            onSearchClick = onSearchClick,
            onEventClick = onEventClick,
            modifier = modifier,
            navigateToAuthorization = navigateToAuthorization
        )

        else -> navigateToAuthorization()
    }
}