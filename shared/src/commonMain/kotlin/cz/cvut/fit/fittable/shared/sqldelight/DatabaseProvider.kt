package cz.cvut.fit.fittable.shared.sqldelight

import app.cash.sqldelight.db.SqlDriver
import cz.cvut.fit.fittable.TimetableDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): TimetableDatabase {
    val driver = driverFactory.createDriver()
    return TimetableDatabase(driver)
}
