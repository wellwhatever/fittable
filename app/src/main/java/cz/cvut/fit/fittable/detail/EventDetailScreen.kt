package cz.cvut.fit.fittable.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
                // TODO add error state with refresh to this screen
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
    detail: EventDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EventTitle(
            course = detail.course,
            sequenceNumber = detail.sequenceNumber
        )
        Location(
            room = detail.room
        )
        Capacity(
            occupied = detail.occupied.toString(),
            capacity = detail.capacity.toString(),
            parallel = detail.parallel
        )
    }
}

@Composable
private fun EventTitle(
    course: String,
    sequenceNumber: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
            contentDescription = stringResource(R.string.event_detail_course),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = course,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(
                id = R.string.event_detail_lectures_count,
                sequenceNumber
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun Location(
    room: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location_on_24),
            contentDescription = stringResource(R.string.event_detail_location),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = room,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Capacity(
    occupied: String,
    capacity: String,
    parallel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_people_24),
            contentDescription = stringResource(id = R.string.event_detail_capacity),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = occupied,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.event_detail_students),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = capacity,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.event_detail_capacity),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "#$parallel",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.event_detail_parallel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun Teachers(
    teachers: List<String>,
    modifier: Modifier = Modifier,
) {

}
