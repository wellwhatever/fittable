package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterDomain
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class EventConflictMergerTest : FunSpec({
    test("mergeConflicts should return correct TimetableItems") {
        val eventConverterDomain = mockk<EventConverterDomain>()
        val eventConflictMerger = EventConflictMerger(eventConverterDomain)

        coEvery {
            eventConverterDomain.toTimetableItem(FakeData.events.first())
        } returns FakeData.fakeSingleEvent
        runTest {
            val result = eventConflictMerger.mergeConflicts(FakeData.events, FakeData.day)
            val expectedTimetableItems = FakeData.mergedAndFilteredEvents
            result shouldContainExactly expectedTimetableItems
        }
    }
})