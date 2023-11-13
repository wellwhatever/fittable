package cz.cvut.fit.fittable.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.R
import cz.cvut.fit.fittable.core.ui.Loading
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import org.koin.androidx.compose.getViewModel

@Composable
fun EventDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: EventDetailViewModel = getViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    with(state.value) {
        when (this) {
            is EventDetailState.Loading -> Loading()
            is EventDetailState.Error -> {
                // TODO add error state with refresh to this screen}
            }

            is EventDetailState.Content -> EventDetailInternal(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                detail = eventDetail
            )
        }
    }
}

@Composable
private fun EventDetailInternal(
    modifier: Modifier = Modifier,
    detail: EventDetail
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.event_detail_lectures_count, detail.sequenceNumber),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = detail.course,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = detail.room,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(
                    text = detail.occupied.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "students",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column {
                Text(
                    text = detail.capacity.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "capacity",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column {
                Text(
                    text = detail.parallel,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "parallel",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}