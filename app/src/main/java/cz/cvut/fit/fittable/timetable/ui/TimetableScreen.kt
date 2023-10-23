package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel

@Composable
fun TimetableScreen(
    timetableViewModel: TimetableViewModel = getViewModel(),
) {
    val state = timetableViewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxWidth()) {
        TimetableContent(token = state.value.orEmpty())
    }
}

@Composable
fun TimetableContent(
    token: String,
) {
    Column {
        Text(
            token,
        )
    }
}
