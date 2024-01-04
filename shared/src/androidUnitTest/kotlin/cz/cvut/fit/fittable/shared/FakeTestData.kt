package cz.cvut.fit.fittable.shared

import cz.cvut.fit.fittable.shared.authorization.data.remote.model.TokenInformation
import cz.cvut.fit.fittable.shared.detail.domain.model.EventDetail
import cz.cvut.fit.fittable.shared.search.data.remote.model.SearchResultType
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Event
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventType
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Links
import cz.cvut.fit.fittable.shared.timetable.domain.model.EventDomain
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableConflictContent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableEventContainer
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSingleEvent
import cz.cvut.fit.fittable.shared.timetable.domain.model.TimetableSpacer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.EventDetail as RemoteEventDetail

object FakeData {
    val searchType = SearchResultType.COURSE
    val eventId = "123"
    val instant = Instant.parse("2020-08-30T18:00Z")
    val endsAt = Instant.parse("2020-08-30T19:00Z")
    val day =
        instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val StartOfDayInstant = day
        .atTime(0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault())
    val EndOfDayInstant = day
        .atTime(23, 59, 59)
        .toInstant(TimeZone.currentSystemDefault())
    val from = StartOfDayInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val to = EndOfDayInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val token = "fakeToken"
    val userName = "john_doe"
    val fakeClientId = "fake_client_id"

    val tokenInformation = TokenInformation(
        aud = listOf(),
        authorities = listOf(),
        clientId = fakeClientId,
        exp = Clock.System.now().epochSeconds + 10000,
        scope = listOf(),
        userName = userName
    )

    val eventRemote = Event(
        deleted = false,
        endsAt = endsAt,
        eventType = EventType.EVENT,
        id = 123123L,
        occupied = 10,
        capacity = 12,
        parallel = "12",
        sequenceNumber = 12,
        startsAt = instant,
        name = "test",
        links = Links(
            course = "Test Course",
            room = "Test Room"
        )
    )

    val eventDomain = EventDomain(
        title = "Test Course",
        room = "Test Room",
        id = "123123",
        start = instant,
        end = endsAt
    )

    val eventDetailRemote = RemoteEventDetail(
        event = eventRemote
    )

    val eventDetailDomain = with(eventDetailRemote.event) {
        EventDetail(
            course = links?.course.orEmpty(),
            room = links?.room.orEmpty(),
            sequenceNumber = sequenceNumber.toString(),
            capacity = capacity ?: 0,
            occupied = occupied,
            eventType = eventType,
            parallel = parallel,
            teacherUsernames = links?.teachers.orEmpty(),
            starts = startsAt,
            ends = endsAt
        )
    }

    val fakeSingleEvent = TimetableSingleEvent(
        title = "course",
        room = "room",
        id = "123",
        start = instant,
        end = instant + 1.hours
    )

    val events = listOf(
        EventDomain(
            title = "course",
            room = "room",
            id = "123",
            start = instant,
            end = instant + 1.hours
        )
    )
    val mergedAndFilteredEvents = listOf(
        TimetableSpacer(13.hours),
        TimetableEventContainer(
            start = instant,
            end = instant + 1.hours,
            events = listOf(
                TimetableConflictContent(
                    spacerStart = null,
                    spacerEnd = null,
                    event = fakeSingleEvent
                )
            )
        ),
        TimetableSpacer(1.hours)
    )
}