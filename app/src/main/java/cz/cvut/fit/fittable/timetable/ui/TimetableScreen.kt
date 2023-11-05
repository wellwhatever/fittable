@file:OptIn(ExperimentalFoundationApi::class)

package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.app.ui.theme.md_theme_light_outline
import cz.cvut.fit.fittable.timetable.domain.model.TimetableEvent
import cz.cvut.fit.fittable.timetable.domain.model.TimetableGridHour
import cz.cvut.fit.fittable.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.timetable.domain.model.TimetableSpacer
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt

private val defaultHourHeight = 64.dp

@Composable
fun TimetableScreen(
    timetableViewModel: TimetableViewModel = getViewModel(),
) {
    val state = timetableViewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        with(state.value) {
            if (this is TimetableContent) {
                TimetableInternal(
                    hoursGrid = hoursGrid,
                    events = events,
                    onEventClick = timetableViewModel::onEventClick
                )
            } else {
                CircularProgressIndicator()
            }
        }

    }
}

@Composable
internal fun TimetableInternal(
    hoursGrid: List<TimetableGridHour>,
    events: List<TimetableItem>,
    onEventClick: (event: TimetableEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateGrid = rememberLazyListState()
    val stateTimetable = rememberLazyListState()
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(
            modifier = modifier
        ) {
            VerticalDivider(modifier = Modifier.padding(start = 100.dp))
            LazyColumn(
                state = stateGrid,
                userScrollEnabled = false,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items = hoursGrid, key = { item -> item.hour }) {
                    HourGridItem(hour = it.hour)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = stateTimetable
            ) {
                // TODO fix key of this item
                items(
                    items = events,
                ) {
                    when (it) {
                        is TimetableEvent -> {
                            EventItem(
                                event = it,
                                modifier = Modifier
                                    .padding(start = 100.dp)
                                    .fillMaxWidth(),
                                onEventClick = onEventClick
                            )
                        }

                        is TimetableSpacer -> {
                            EventSpacer(eventSpacer = it)
                        }
                    }
                }
            }
        }
    }
    val offset =
        snapshotFlow { stateTimetable.firstVisibleItemIndex to stateTimetable.firstVisibleItemScrollOffset }


    LaunchedEffect(Unit) {
        offset.collect {
            stateGrid.scrollToItem(it.first, it.second)
        }
    }
}

@Composable
private fun EventItem(
    event: TimetableEvent,
    onEventClick: (event: TimetableEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .height(event.convertToHeight(defaultHourHeight.value.roundToInt()).dp)
            .clip(MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.primary)
            .clickable(onClick = { onEventClick(event) })
            .padding(8.dp)
    ) {
        Text(
            text = event.title,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun EventSpacer(
    eventSpacer: TimetableSpacer,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.height(
            eventSpacer.convertToHeight(defaultHourHeight.value.toInt()).dp
        )
    )
}

@Composable
private fun HourGridItem(
    hour: String,
    modifier: Modifier = Modifier,
    hourSize: Dp = defaultHourHeight,
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp)
            .height(hourSize)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier
                .weight(0.2f)
                .padding(end = 8.dp),
            text = hour,
            color = md_theme_light_outline
        )
        Divider(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = DIVIDER_ALPHA)
        )
    }
}

private const val DIVIDER_ALPHA = 0.12f

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = DIVIDER_ALPHA),
    thickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}
