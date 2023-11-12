package cz.cvut.fit.fittable.timetable.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableItem
import kotlin.math.roundToInt

internal object TimetableItemOffsetCalculator {
    private fun calculateItemsOffsetInPxUpToIndex(
        items: List<TimetableItem>,
        currentIndex: Int,
        defaultHourHeight: Int,
        density: Density,
    ): Float {
        if (currentIndex <= 0) return 0f

        return with(density) {
            val totalOffset = items.take(currentIndex).sumOf {
                it.convertToHeight(defaultHourHeight)
            }
            totalOffset.dp.toPx()
        }
    }

    fun calculateItemOffsetFromStart(
        items: List<TimetableItem>,
        firstVisibleItemOffset: Int,
        firstVisibleItemIndex: Int,
        defaultHourHeight: Int,
        density: Density,
    ): Int {
        val offsetBeforeItem = calculateItemsOffsetInPxUpToIndex(
            items = items,
            currentIndex = firstVisibleItemIndex,
            defaultHourHeight = defaultHourHeight,
            density = density
        )
        return (offsetBeforeItem + firstVisibleItemOffset).roundToInt()
    }
}