package cz.cvut.fit.fittable

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cz.cvut.fit.fittable.multiplatform.Greeting
import cz.cvut.fit.fittable.ui.theme.FittableTheme
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            modules(androidModule)
        }
        setContent {
            FittableApp()
        }
    }
}

@Composable
fun FittableApp(modifier: Modifier = Modifier) {
    FittableTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val context = LocalContext.current
            val serviceConfig = AuthorizationServiceConfiguration(
                Uri.parse("https://auth.fit.cvut.cz/oauth/oauth/authorize"), // authorization endpoint
                Uri.parse("https://auth.fit.cvut.cz/oauth/oauth/token"), // token endpoint
            )
            val clientId = "69be8dc8-2117-4735-adc0-19102e8ef456"
            val clientSecret = "Dhm6bhEcWB6rvKdorB69HGQQ8N5Klaux"
            val redirectUri = Uri.parse("fit-timetable://oauth/callback")

            val authService = AuthorizationService(context)

            val result = remember { mutableStateOf<AuthorizationResponse?>(null) }
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    Log.e("Result", "code: ${it.resultCode}")
                    Log.e("Result", "data: ${it.data?.data}")
                    Log.e("Result", "has_access_token: ${it.data?.hasExtra("access_token")}")
                    val data: Uri? = it.data?.data
                    if (data != null) {
                        // Extract the access token or other parameters from the URL
                        val accessToken: String? = getAccessTokenFromUri(data)
                        Log.e("AccessToken:", "token: $accessToken")
                        Log.e("Query:", "value: ${data.path}")
                        // Handle the token or any other response as needed
                    }
                    val authorizationResponse: AuthorizationResponse? =
                        it.data?.let { intent -> AuthorizationResponse.fromIntent(intent) }
                    val error = it.data?.let { intent -> AuthorizationException.fromIntent(intent) }
//
                    Log.e("Token", "token: ${authorizationResponse?.accessToken}")
                    Log.e("Error", "error: ${error?.error}")
                }
            Button(
                onClick = {
                    val builder = AuthorizationRequest.Builder(
                        serviceConfig,
                        clientId,
                        ResponseTypeValues.TOKEN,
                        redirectUri,
                    ).setScope("urn:ctu:oauth:sirius:personal:read")

                    val oauth2URL =
                        "https://auth.fit.cvut.cz/oauth/oauth/authorize?response_type=token&client_id=$clientId&redirect_uri=$redirectUri&scope=urn:zuul:oauth:kosapi:public.read"

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(oauth2URL))

                    val authRequest: AuthorizationRequest = builder.build()

                    val authIntent = authService.getAuthorizationRequestIntent(authRequest)

                    launcher.launch(authIntent)
                },
                content = { Text(Greeting().greet()) },
            )
        }
    }
}

fun getAccessTokenFromUri(uri: Uri): String? {
    val fragment = uri.fragment ?: ""
    val params = fragment.split("&")
    for (param in params) {
        val parts = param.split("=")
        if (parts.size == 2 && parts[0] == "access_token") {
            return parts[1]
        }
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FittableTheme {
    }
}
