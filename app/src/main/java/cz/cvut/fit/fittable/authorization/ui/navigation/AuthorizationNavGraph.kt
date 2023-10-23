package cz.cvut.fit.fittable.authorization.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cz.cvut.fit.fittable.authorization.ui.AuthorizationScreen

const val AUTHORIZATION_NAVIGATION_GRAPH = "auth_nav_graph"
const val AUTHORIZATION_ROUTE = "auth_route"

fun NavController.navigateToAuthorizationNavGraph(navOptions: NavOptions? = null) {
    this.navigate(AUTHORIZATION_NAVIGATION_GRAPH, navOptions)
}

fun NavGraphBuilder.authorizationNavGraph(
    onSuccessfulTokenReceive: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = AUTHORIZATION_NAVIGATION_GRAPH,
        startDestination = AUTHORIZATION_ROUTE,
    ) {
        composable(route = AUTHORIZATION_ROUTE) {
            AuthorizationScreen(onSuccessfulTokenReceive = onSuccessfulTokenReceive)
        }
        nestedGraphs()
    }
}
