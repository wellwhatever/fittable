package cz.cvut.fit.fittable.shared.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import cz.cvut.fit.fittable.TimetableDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            TimetableDatabase.Schema.synchronous(),
            "TimetableDatabase.db"
        )
    }
}
