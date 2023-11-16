package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration

sealed interface TimetableItem {
    val duration: Duration
    fun convertToHeight(hourHeight: Int): Int =
        abs(((duration.inWholeMinutes / 60f) * hourHeight).roundToInt())
}

abstract class TimetableEvent : TimetableItem {
    abstract val start: Instant
    abstract val end: Instant
    override val duration: Duration
        get() = end - start
}

data class TimetableConflict(
    val conflictedEvents: List<TimetableEvent>,
    override val start: Instant,
    override val end: Instant,
) : TimetableEvent()

data class TimetableConflictItem(
    val spacerStart: TimetableSpacer?,
    val spacerEnd: TimetableSpacer?,
    val event: TimetableEvent,
    override val start: Instant,
    override val end: Instant
) : TimetableEvent()

data class TimetableSingleEvent(
    val title: String,
    val room: String,
    override val start: Instant,
    override val end: Instant,
) : TimetableEvent() {
    val day = start.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
}

data class TimetableSpacer(
    override val duration: Duration,
) : TimetableItem

data class TimetableHour(
    val hour: String,
)