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

    val day: Int
        get() = start.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
}

data class TimetableEventContainer(
    val events: List<TimetableConflictContent>,
    override val start: Instant,
    override val end: Instant,
) : TimetableEvent()

data class TimetableConflictContent(
    val spacerStart: TimetableSpacer? = null,
    val spacerEnd: TimetableSpacer? = null,
    val event: TimetableSingleEvent,
)

data class TimetableSingleEvent(
    val title: String,
    val room: String,
    val id: String,
    override val start: Instant,
    override val end: Instant,
) : TimetableEvent()

data class TimetableSpacer(
    override val duration: Duration,
) : TimetableItem

data class TimetableHour(
    val hour: String,
)