package cz.cvut.fit.fittable.shared.timetable.domain

import cz.cvut.fit.fittable.shared.timetable.data.EventsCacheRepository
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class CachePersonalEventsUseCaseTest : FunSpec({

    test("caches events within the specified radius") {
        // Mock dependencies
        val eventsRepository = mockk<EventsCacheRepository>()

        // Create an instance of the use case with the mocked clock
        val cachePersonalEventsUseCase = CachePersonalEventsUseCase(eventsRepository)

        // Define test data
        val currentDate = Clock.System.now()
        val from = (currentDate - 14.days)
            .toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).date // 14 days before now
        val to = (currentDate + 14.days)
            .toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).date // 14 days after now

        coEvery {
            eventsRepository.cacheEvents(
                from = from,
                to = to
            )
        } returns Unit

        cachePersonalEventsUseCase()

        // Verify that eventsRepository.cacheEvents was called with the correct parameters
        coVerify {
            eventsRepository.cacheEvents(
                from = from,
                to = to
            )
        }
    }
})