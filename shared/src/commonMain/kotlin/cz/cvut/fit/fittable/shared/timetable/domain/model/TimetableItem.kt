package cz.cvut.fit.fittable.shared.timetable.domain.model

import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration

sealed interface TimetableItem {
    val duration: Duration
    fun convertToHeight(hourHeight: Int) =
        ((duration.inWholeMinutes / 60f) * hourHeight).roundToInt()
}


data class TimetableEvent(
    val title: String,
    val room: String,
    val start: Instant,
    val end: Instant,
    override val duration: Duration
) : TimetableItem

data class TimetableSpacer(
    override val duration: Duration
) : TimetableItem

data class TimetableGridHour(
    val hour: String,
)