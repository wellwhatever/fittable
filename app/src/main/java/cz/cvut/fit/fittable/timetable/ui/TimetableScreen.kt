@file:OptIn(ExperimentalFoundationApi::class)

package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.fittable.app.ui.theme.md_theme_light_outline
import cz.cvut.fit.fittable.timetable.domain.model.CalendarGridHour
import org.koin.androidx.compose.getViewModel

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
                TimetableInternal(hoursGrid = hoursGrid)
            } else {
                CircularProgressIndicator()
            }
        }

    }
}

@Composable
internal fun TimetableInternal(
    hoursGrid: List<CalendarGridHour>
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box {
            VerticalDivider(modifier = Modifier.padding(start = 100.dp))
            LazyColumn {
                items(
                    items = hoursGrid,
                    key = { key -> key.hour }
                ) {
                    HourGridItem(hour = it.hour)
                }
            }
        }
    }
}

@Composable
private fun HourGridItem(
    hour: String,
    modifier: Modifier = Modifier,
    hourSize: Dp = 64.dp,
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .height(hourSize)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
