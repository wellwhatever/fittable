package cz.cvut.fit.fittable.shared.sqldelight

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import cz.cvut.fit.fittable.TimetableDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            TimetableDatabase.Schema.synchronous(),
            context,
            "TimetableDatabase.db"
        )
    }
}