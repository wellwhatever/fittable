package cz.cvut.fit.fittable.shared.detail.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.datetime.Clock

class GetEventByIdUseCaseTest : FunSpec({

    test("returns EventDetail with correct values") {
        // Mock dependencies
        val eventsRepository = mockk<EventsCacheRepository>()

        // Create an instance of the use case
        val getEventByIdUseCase = GetEventByIdUseCase(eventsRepository)


        val now = Clock.System.now()
        // Define test data
        val eventId = "123"
        val fakeEvent = FakeData.eventDetailRemote

        // Stub eventsRepository to return a fake event
        coEvery {
            eventsRepository.getEvent(eventId)
        } returns fakeEvent

        // Call the use case
        val result = getEventByIdUseCase(eventId)

        // Verify that the result is an EventDetail with the expected values
        result shouldBe FakeData.eventDetailDomain

        // Verify that other methods were called as expected (if necessary)
        coVerify {
            eventsRepository.getEvent(eventId)
        }
    }
})