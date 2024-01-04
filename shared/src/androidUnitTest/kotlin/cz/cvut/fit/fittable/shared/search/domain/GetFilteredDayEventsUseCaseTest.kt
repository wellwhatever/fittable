package cz.cvut.fit.fittable.shared.search.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.timetable.domain.EventConflictMerger
import cz.cvut.fit.fittable.shared.timetable.domain.EventsDayBoundsCalculator
import cz.cvut.fit.fittable.shared.timetable.domain.GetCoursesEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetRoomEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.GetUserEventsUseCase
import cz.cvut.fit.fittable.shared.timetable.domain.model.RequestDateBounds
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

class GetFilteredDayEventsUseCaseTest : FunSpec({

    test("returns merged and filtered events for the given type, id, and day") {
        // Mock dependencies
        val getCoursesEvents = mockk<GetCoursesEventsUseCase>()
        val getRoomEvents = mockk<GetRoomEventsUseCase>()
        val getUserEvents = mockk<GetUserEventsUseCase>()
        val eventsDayBoundsCalculator = mockk<EventsDayBoundsCalculator>()
        val eventsMerger = mockk<EventConflictMerger>()

        // Create an instance of the use case
        val getFilteredDayEventsUseCase = GetFilteredDayEventsUseCase(
            getCoursesEvents,
            getRoomEvents,
            getUserEvents,
            eventsDayBoundsCalculator,
            eventsMerger
        )

        // Stub eventsDayBoundsCalculator to return the expected bounds
        every {
            eventsDayBoundsCalculator.getRequestDataBounds(FakeData.day)
        } returns RequestDateBounds(
            FakeData.from,
            FakeData.to
        )

        // Stub the appropriate use case based on the SearchResultType
        coEvery {
            when (FakeData.searchType) {
                SearchResultType.COURSE -> getCoursesEvents(
                    course = FakeData.eventId,
                    from = FakeData.from,
                    to = FakeData.to
                )

                SearchResultType.PERSON -> getUserEvents(
                    username = FakeData.eventId,
                    from = FakeData.from,
                    to = FakeData.to
                )

                SearchResultType.ROOM -> getRoomEvents(
                    room = FakeData.eventId,
                    from = FakeData.from,
                    to = FakeData.to
                )
            }
        } returns FakeData.events

        // Stub eventsMerger to return the merged and filtered events
        coEvery {
            eventsMerger.mergeConflicts(FakeData.events, FakeData.day)
        } returns FakeData.mergedAndFilteredEvents

        // Call the use case
        val result =
            getFilteredDayEventsUseCase(FakeData.searchType, FakeData.eventId, FakeData.day)

        // Verify that the result is the merged and filtered events
        result shouldBe FakeData.mergedAndFilteredEvents
    }
})