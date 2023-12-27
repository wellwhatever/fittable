package cz.cvut.fit.fittable.shared.timetable.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cz.cvut.fit.fittable.shared.timetable.data.local.converter.EventConverterLocal
import cz.cvut.fit.fittable.shared.timetable.data.remote.model.Event
import cz.cvut.fit.fittable.sqldelight.TimetableDatabaseQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventsLocalDataSource(
    private val eventsQueries: TimetableDatabaseQueries,
    private val eventConverter: EventConverterLocal,
    private val ioDispatcher: CoroutineDispatcher,
) {
    fun getEventsFlow(): Flow<List<Event>> =
        eventsQueries.selectAll().asFlow().mapToList(ioDispatcher).map { entities ->
            entities.map { eventConverter.toRemote(it) }
        }

    suspend fun refreshEvents(new: List<Event>) {
        eventsQueries.deleteAll()
        new.forEach {
            eventsQueries.insertEntity(eventConverter.toEntity(it))
        }
    }
}
