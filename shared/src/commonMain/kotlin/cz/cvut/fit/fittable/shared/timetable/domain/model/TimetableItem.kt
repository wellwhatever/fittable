package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Duration

sealed interface TimetableItem {
    val id: String
    val duration: Duration
    fun convertToHeight(hourHeight: Int) =
        ((duration.inWholeMinutes / 60f) * hourHeight).roundToInt()
}

data class TimetableEvent(
    val title: String,
    val room: String,
    val start: Instant,
    val end: Instant,
    override val duration: Duration,
    override val id: String
) : TimetableItem {
    val day = start.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
}

data class TimetableSpacer(
    override val duration: Duration,
    override val id: String
) : TimetableItem

data class TimetableHour(
    val hour: String,
)