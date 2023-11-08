package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun CalendarHeader(
    startMonth: LocalDate,
    endMonth: LocalDate,
    currentMonth: LocalDate,
    modifier: Modifier = Modifier,
) {
    val expanded = remember { mutableStateOf(false) }
    val daysOfWeek = remember { mutableStateOf(daysOfWeek()) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val monthDisplayName =
            currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        Text(
            modifier = Modifier
                .clickable { expanded.value = !expanded.value }
                .fillMaxWidth()
                .padding(8.dp),
            text = monthDisplayName,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
        AnimatedVisibility(visible = expanded.value) {
            CalendarHeaderInternal(
                startMonth = startMonth,
                endMonth = endMonth,
                currentMonth = currentMonth,
                daysOfWeek = daysOfWeek.value
            )
        }
    }
}

@Composable
internal fun CalendarHeaderInternal(
    startMonth: LocalDate,
    endMonth: LocalDate,
    currentMonth: LocalDate,
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    val state = rememberCalendarState(
        startMonth = startMonth.toJavaLocalDate().yearMonth,
        endMonth = endMonth.toJavaLocalDate().yearMonth,
        firstVisibleMonth = currentMonth.toJavaLocalDate().yearMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title here
        HorizontalCalendar(
            state = state,
            dayContent = { Day(it) }
        )
    }
}

@Composable
fun Day(day: CalendarDay, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = MaterialTheme.colorScheme.onPrimary
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
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}