package cz.cvut.fit.fittable.authorization.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cvut.fit.fittable.R
import cz.cvut.fit.fittable.app.ui.theme.FittableTheme
import net.openid.appauth.AuthorizationService
import org.koin.androidx.compose.getViewModel
import org.koin.compose.koinInject

@Composable
fun AuthorizationScreen(
    authorizationService: AuthorizationService = koinInject(),
    viewModel: AuthorizationViewModel = getViewModel(),
) {
    val launcher = registerAuthorizationActivityLauncher(
        onSuccessfulActivityResult = viewModel::onReceiveTokenSuccess,
        onActivityResultError = viewModel::onReceiveTokenError,
    )
    LaunchedEffect(Unit) {
        viewModel.navigateToAuthorization.collect { request ->
            val intent = authorizationService.getAuthorizationRequestIntent(request)
            launcher.launch(intent)
        }
    }
    AuthorizationContent(onAuthorizeClick = viewModel::onAuthorizeClick)
}

@Composable
private fun registerAuthorizationActivityLauncher(
    onSuccessfulActivityResult: (String) -> Unit,
    onActivityResultError: (String) -> Unit,
): ManagedActivityResultLauncher<Intent, ActivityResult> =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: String = it.data?.data?.fragment ?: ""
            onSuccessfulActivityResult(data)
        } else {
            onActivityResultError(ActivityResult.resultCodeToString(it.resultCode))
        }
    }

@Composable
private fun AuthorizationContent(
    modifier: Modifier = Modifier,
    onAuthorizeClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // TODO fix this image!!!
        Image(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(id = R.drawable.ic_unitable_logo_title),
            contentDescription =
            stringResource(id = R.string.authorization_screen_title),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = stringResource(id = R.string.authorization_screen_message),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth(),
            onClick = onAuthorizeClick,
        ) {
            Text(
                text = stringResource(id = R.string.authorization_button_label),
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_ctu_logo_background),
            contentDescription = stringResource(id = R.string.authorization_logo_description),
        )
    }
}

@Composable
@Preview
private fun AuthorizationContentPreview() {
    FittableTheme {
        Surface {
            AuthorizationContent {}
        }
    }
}
