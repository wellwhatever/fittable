package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import cz.cvut.fit.fittable.shared.timetable.domain.model.RequestDateBounds
import io.kotest.assertions.any
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class GetCachedEventsUseCaseTest : FunSpec({
    test("returns merged and filtered events for the given day") {
        // Mock dependencies
        val eventsRepository = mockk<EventsCacheRepository>()
        val eventsDayBoundsCalculator = mockk<EventsDayBoundsCalculator>()
        val eventConflictMerger = mockk<EventConflictMerger>()
        val eventConverterRemote = mockk<EventConverterRemote>()

        // Create an instance of the use case
        val getCachedEventsUseCase = GetCachedEventsUseCase(
            eventsRepository,
            eventsDayBoundsCalculator,
            eventConflictMerger,
            eventConverterRemote
        )

        // Stub eventsDayBoundsCalculator to return the expected bounds
        every {
            eventsDayBoundsCalculator.getRequestDataBounds(FakeData.day)
        } returns RequestDateBounds(FakeData.from, FakeData.to)

        // Stub eventsRepository to return fake remote events
        coEvery {
            eventsRepository.getCachedEvents(from = FakeData.from, to = FakeData.to)
        } returns listOf(FakeData.eventRemote)

        // Stub eventConverterRemote to convert fake remote events to domain events
        every {
            eventConverterRemote.toDomain(any())
        } returns FakeData.eventDomain

        // Stub eventConflictMerger to return the merged and filtered events
        coEvery {
            eventConflictMerger.mergeConflicts(listOf(FakeData.eventDomain), FakeData.day)
        } returns FakeData.mergedAndFilteredEvents

        // Call the use case
        runTest {
            val result = getCachedEventsUseCase(FakeData.day)

            // Verify that the result is the merged and filtered events
            result shouldContainExactly FakeData.mergedAndFilteredEvents
        }
    }
})