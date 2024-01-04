package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import cz.cvut.fit.fittable.shared.timetable.domain.converter.EventConverterRemote
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class GetUserEventsUseCaseTest : FunSpec({

    test("returns mapped events from repository") {
        // Mock dependencies
        val eventsRepository = mockk<EventsCacheRepository>()
        val eventConverterRemote = mockk<EventConverterRemote>()

        // Create an instance of the use case
        val getUserEventsUseCase = GetUserEventsUseCase(eventsRepository, eventConverterRemote)

        // Stub eventsRepository to return fake data
        coEvery {
            eventsRepository.getUserEvents(
                from = FakeData.from,
                to = FakeData.to,
                username = FakeData.userName
            )
        } returns listOf(FakeData.eventRemote)

        // Stub eventConverterRemote to convert fake data
        every {
            eventConverterRemote.toDomain(FakeData.eventRemote)
        } returns FakeData.eventDomain

        // Call the use case
        runTest {
            val result = getUserEventsUseCase(FakeData.userName, FakeData.from, FakeData.to)

            // Verify that the result is the mapped events
            result shouldContainExactly listOf(FakeData.eventDomain)

            // Verify that other methods were called as expected (if necessary)
            coVerify {
                eventsRepository.getUserEvents(
                    from = FakeData.from,
                    to = FakeData.to,
                    username = FakeData.userName
                )
            }
            every {
                eventConverterRemote.toDomain(any())
            }
        }
    }
})