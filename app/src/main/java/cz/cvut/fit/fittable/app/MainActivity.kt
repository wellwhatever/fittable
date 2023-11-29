package cz.cvut.fit.fittable.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.cvut.fit.fittable.app.ui.navigation.AppNavigationGraph
import cz.cvut.fit.fittable.app.ui.theme.FittableTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FittableTheme {
                FittableApp()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun FittableApp(
    modifier: Modifier = Modifier,
    appState: FittableAppState = rememberFittableAppState(),
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        AppNavigationGraph(
            navHostController = appState.navController,
            modifier = modifier,
        )
    }
}

@Composable
fun rememberFittableAppState(
    navController: NavHostController = rememberNavController(),
): FittableAppState = remember(navController) {
    FittableAppState(navController)
}

@Stable
class FittableAppState(
    val navController: NavHostController,
)
