package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import cz.cvut.fit.fittable.R
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun CalendarHeader(
    startMonth: LocalDate,
    endMonth: LocalDate,
    today: LocalDate,
    selected: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expanded = remember { mutableStateOf(false) }
    val daysOfWeek = remember { mutableStateOf(daysOfWeek()) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
    ) {
        val monthDisplayName =
            today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        CalendarTitle(title = monthDisplayName, onClick = {
            expanded.value = expanded.value.not()
        })
        AnimatedVisibility(visible = expanded.value) {
            CalendarHeaderInternal(
                startMonth = startMonth,
                endMonth = endMonth,
                today = today,
                selected = selected,
                daysOfWeek = daysOfWeek.value,
                onDayClick = onDayClick,
            )
        }
    }
}

@Composable
internal fun CalendarTitle(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotationState = remember {
        mutableFloatStateOf(0f)
    }
    val iconRotation by animateFloatAsState(
        targetValue = rotationState.floatValue,
        label = "iconRotation"
    )
    Row(
        modifier = modifier.clickable {
            rotationState.floatValue += 180f
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall
        )
        Icon(
            modifier = Modifier.rotate(iconRotation),
            painter = painterResource(R.drawable.ic_arrow_drop_down_24),
            contentDescription = stringResource(R.string.timetable_header_title_description),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
internal fun CalendarHeaderInternal(
    startMonth: LocalDate,
    endMonth: LocalDate,
    today: LocalDate,
    selected: LocalDate,
    daysOfWeek: List<DayOfWeek>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberCalendarState(
        startMonth = startMonth.toJavaLocalDate().yearMonth,
        endMonth = endMonth.toJavaLocalDate().yearMonth,
        firstVisibleMonth = today.toJavaLocalDate().yearMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title here
        HorizontalCalendar(
            state = state,
            dayContent = {
                if (it.date.month.value == today.month.value) {
                    Day(
                        day = it,
                        onClick = onDayClick,
                        isSelected = it.date == selected.toJavaLocalDate(),
                        isCurrentDay = today.toJavaLocalDate() == it.date
                    )
                }
            }
        )
    }
}

@Composable
fun BoxScope.Day(
    day: CalendarDay,
    isSelected: Boolean,
    isCurrentDay: Boolean,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = when {
        isCurrentDay -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
        isSelected -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f) to MaterialTheme.colorScheme.onTertiary
        else -> Color.Transparent to MaterialTheme.colorScheme.onPrimary
    }

    Box(
        modifier = modifier
            .align(Alignment.Center)
            .size(36.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable {
                with(day.date) {
                    onClick(LocalDate(year, month, dayOfMonth))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = contentColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
internal fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}