package cz.cvut.fit.fittable.timetable.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Duration

sealed interface TimetableItem {
    val duration: Duration
    fun convertToHeight(hourHeight: Int) =
        ((duration.inWholeMinutes / 60f) * hourHeight).roundToInt()
}


data class TimetableEvent(
    val title: String,
    val start: LocalDateTime,
    override val duration: Duration
) : TimetableItem

data class TimetableSpacer(
    override val duration: Duration
) : TimetableItem

data class TimetableGridHour(
    val hour: String,
)