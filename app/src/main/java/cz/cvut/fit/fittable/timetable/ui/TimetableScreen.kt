@file:OptIn(ExperimentalFoundationApi::class)

package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.R
import cz.cvut.fit.fittable.app.ui.theme.FittableTheme
import cz.cvut.fit.fittable.app.ui.theme.md_theme_light_outline
import cz.cvut.fit.fittable.core.ui.HorizontalGridDivider
import cz.cvut.fit.fittable.core.ui.Loading
import cz.cvut.fit.fittable.core.ui.VerticalGridDivider
import cz.cvut.fit.fittable.shared.core.extensions.formatAsHoursAndMinutes
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflictContent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEventContainer
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableHour
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSingleEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours

private val defaultHourHeight = 64.dp

@Composable
fun TimetableScreen(
    onSearchClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
    timetableViewModel: TimetableViewModel = getViewModel(),
) {
    val state = timetableViewModel.uiState.collectAsStateWithLifecycle()

    with(state.value) {
        when (this) {
            is TimetableUiState.Content -> TimetableInternal(
                modifier = modifier,
                hoursGrid = hoursGrid,
                events = events,
                headerState = header,
                onEventClick = onEventClick,
                onDayClick = timetableViewModel::onDayClick,
                onSearchClick = onSearchClick
            )

            is TimetableUiState.Error -> TimetableError(
                modifier = modifier,
                error = error,
                onReloadClick = timetableViewModel::onReloadClick
            )

            TimetableUiState.Loading -> Loading()
        }
    }
}

@Composable
internal fun TimetableInternal(
    hoursGrid: List<TimetableHour>,
    events: List<TimetableItem>,
    onEventClick: (eventId: String) -> Unit,
    onDayClick: (day: LocalDate) -> Unit,
    onSearchClick: () -> Unit,
    headerState: HeaderState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CalendarHeader(
            startMonth = headerState.monthStart,
            endMonth = headerState.monthEnd,
            today = headerState.today,
            selected = headerState.selectedDate,
            onDayClick = onDayClick,
            onSearchClick = onSearchClick
        )
        TimetableGrid(
            hoursGrid = hoursGrid,
            events = events,
            onEventClick = onEventClick
        )
    }
}

@Composable
private fun TimetableGrid(
    hoursGrid: List<TimetableHour>,
    events: List<TimetableItem>,
    onEventClick: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateGrid = rememberLazyListState()
    val stateTimetable = rememberLazyListState()

    val timetableOffset =
        snapshotFlow {
            TimetableStateOffset(
                stateTimetable.firstVisibleItemIndex,
                stateTimetable.firstVisibleItemScrollOffset
            )
        }

    val density = LocalDensity.current
    LaunchedEffect(events) {
        timetableOffset.collect {
            val totalOffset = TimetableItemOffsetCalculator.calculateItemOffsetFromStart(
                items = events,
                firstVisibleItemIndex = it.firstVisibleItemIndex,
                firstVisibleItemOffset = it.firstVisibleItemScrollOffset,
                defaultHourHeight = defaultHourHeight.value.roundToInt(),
                density = density
            )
            stateGrid.scrollToItem(0, totalOffset)
        }
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(
            modifier = modifier
        ) {
            VerticalGridDivider(modifier = Modifier.padding(start = 100.dp))
            TimetableHoursGrid(hours = hoursGrid, state = stateGrid)
            TimetableEventsGrid(
                events = events,
                state = stateTimetable,
                onEventClick = onEventClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TimetableEventsGrid(
    events: List<TimetableItem>,
    state: LazyListState,
    onEventClick: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = events,
        label = "Timetable",
        transitionSpec = {
            val item = targetState.filterIsInstance<TimetableEventContainer>().firstOrNull()
            val prevItem = initialState.filterIsInstance<TimetableEventContainer>().firstOrNull()
            when {
                item == null || prevItem == null -> fadeIn() togetherWith fadeOut()
                item.day > prevItem.day -> slideInHorizontally(
                    initialOffsetX = { it }) + fadeIn() togetherWith slideOutHorizontally(
                    targetOffsetX = { -it }) + fadeOut()

                else -> slideInHorizontally(
                    initialOffsetX = { -it }) + fadeIn() togetherWith slideOutHorizontally(
                    targetOffsetX = { it }) + fadeOut()
            }

        }
    ) {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = it
            ) {
                when (it) {
                    is TimetableSpacer -> EventSpacer(eventSpacer = it)
                    is TimetableEventContainer -> TimetableConflict(
                        modifier = Modifier.padding(start = 100.dp),
                        conflict = it,
                        onEventClick = onEventClick
                    )

                    else -> {
                        // no-op
                    }
                }
            }
        }
    }
}

@Composable
private fun TimetableConflict(
    conflict: TimetableEventContainer,
    onEventClick: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val height = conflict.convertToHeight(defaultHourHeight.value.roundToInt()).dp
    Row(
        modifier = modifier.height(height),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        conflict.events.forEach { event ->
            Column(
                modifier = Modifier
                    .height(height)
                    .weight(1f)
            ) {
                event.spacerStart?.let {
                    EventSpacer(eventSpacer = it)
                }
                EventItem(
                    event = event.event,
                    onEventClick = onEventClick
                )
                event.spacerEnd?.let {
                    EventSpacer(eventSpacer = it)
                }
            }
        }
    }
}

@Composable
private fun TimetableHoursGrid(
    hours: List<TimetableHour>,
    state: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        userScrollEnabled = false,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items = hours, key = { item -> item.hour }) {
            HourGridItem(hour = it.hour)
        }
    }
}

@Composable
private fun EventItem(
    event: TimetableSingleEvent,
    onEventClick: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
    eventHeight: Int? = null,
) {
    val height =
        eventHeight?.dp ?: event.convertToHeight(defaultHourHeight.value.roundToInt()).dp
    Row(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .padding(1.dp)
            .clip(MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.primary)
            .clickable(onClick = { onEventClick(event.id) })
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = event.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = event.room,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        val startFormatted = event.start.formatAsHoursAndMinutes()
        val endFormatted = event.end.formatAsHoursAndMinutes()
        Text(
            text = stringResource(
                R.string.timetable_time_duration,
                startFormatted,
                endFormatted
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Start
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
        HorizontalGridDivider()
    }
}

@Composable
private fun TimetableError(
    error: String,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(error)
        Button(onClick = onReloadClick) {
            Text(stringResource(id = R.string.timetable_reload_button_hint))
        }
    }
}

@Preview
@Composable
private fun TimetablePreview() {
    val start = Instant.parse("2023-11-16T16:15:00.000+01:00")
    val date = start.toLocalDateTime(TimeZone.currentSystemDefault()).date
    Surface {
        FittableTheme {
            TimetableInternal(
                hoursGrid = (7..24).map {
                    TimetableHour("$it:00")
                },
                events = listOf(
                    TimetableEventContainer(
                        events = listOf(
                            TimetableConflictContent(
                                event = TimetableSingleEvent(
                                    title = "BI-AG1",
                                    room = "T9:301",
                                    id = "666",
                                    start = start,
                                    end = start + 1.hours
                                ),
                            )
                        ),
                        start = start,
                        end = start + 1.hours
                    )
                ),
                headerState = HeaderState(
                    monthStart = date,
                    monthEnd = date,
                    today = date,
                    selectedDate = date
                ),
                onEventClick = { },
                onDayClick = { },
                onSearchClick = { }
            )
        }
    }
}
