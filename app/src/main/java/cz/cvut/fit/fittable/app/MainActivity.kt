package cz.cvut.fit.fittable.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.cvut.fit.fittable.app.di.androidModule
import cz.cvut.fit.fittable.app.ui.navigation.AppNavigationGraph
import cz.cvut.fit.fittable.app.ui.theme.FittableTheme
import cz.cvut.fit.fittable.authorization.di.authorizationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(androidModule, authorizationModule)
        }
        setContent {
            FittableTheme {
                FittableApp()
            }
        }
    }
}

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

// @Composable
// fun Test() {
//    val context = LocalContext.current
//    val serviceConfig = AuthorizationServiceConfiguration(
//        Uri.parse("https://auth.fit.cvut.cz/oauth/oauth/authorize"), // authorization endpoint
//        Uri.parse("https://auth.fit.cvut.cz/oauth/oauth/token"), // token endpoint
//    )
//    val clientId = "69be8dc8-2117-4735-adc0-19102e8ef456"
//    val redirectUri = Uri.parse("fit-timetable://oauth/callback")
//
//    val authService = AuthorizationService(context)
//
//    val result = remember { mutableStateOf<AuthorizationResponse?>(null) }
//    val launcher =
//        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val data: String = it.data?.data?.fragment ?: ""
//            if (data != null) {
//                // Parse token response!!!
//            }
//        }
//    Button(
//        onClick = {
// //            launcher.launch(authIntent)
//        },
//        content = { Text(Greeting().greet()) },
//    )
// }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FittableTheme {
    }
}
