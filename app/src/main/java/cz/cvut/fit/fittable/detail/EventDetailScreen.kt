package cz.cvut.fit.fittable.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.R
import cz.cvut.fit.fittable.core.ui.Loading
import cz.cvut.fit.fittable.shared.core.extensions.formatAsHoursAndMinutes
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import org.koin.androidx.compose.getViewModel

@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventDetailViewModel = getViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val topBarState = (state.value as? EventDetailState.Content)
    EventDetailTopBar(
        lecture = topBarState?.eventDetail?.course.orEmpty(),
        lectureNumber = topBarState?.eventDetail?.sequenceNumber.orEmpty(),
        onBackClick = onBack,
        eventType = topBarState?.eventDetail?.eventType?.name.orEmpty()
    ) {
        with(state.value) {
            when (this) {
                is EventDetailState.Loading -> Loading(
                    modifier = modifier.fillMaxSize(),
                )

                is EventDetailState.Error -> EventDetailError(
                    onReloadClick = viewModel::onReloadClick,
                    modifier = modifier
                )

                is EventDetailState.Content -> EventDetailInternal(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    detail = eventDetail
                )
            }
        }
    }
}

@Composable
private fun EventDetailTopBar(
    lecture: String,
    lectureNumber: String,
    eventType: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onBackClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ),
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = lecture,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Text(
                    text = eventType,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "#$lectureNumber",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        content()
    }
}

@Composable
private fun EventDetailError(
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_error_outline_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "error"
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.timetable_unknown_error),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onReloadClick) {
            Text(stringResource(id = R.string.timetable_reload_button_hint))
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
        EventDate(
            startsDate = detail.startsDate.toString(),
            startsTime = detail.starts.formatAsHoursAndMinutes(),
            endsTime = detail.ends.formatAsHoursAndMinutes(),
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
private fun EventDate(
    startsDate: String,
    startsTime: String,
    endsTime: String,
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
            text = startsDate,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = "$startsTime-$endsTime",
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
