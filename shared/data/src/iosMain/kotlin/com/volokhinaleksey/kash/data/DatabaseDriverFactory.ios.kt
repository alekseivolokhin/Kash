package com.volokhinaleksey.kash.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.volokhinaleksey.kash.data.db.KashDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(KashDatabase.Schema, "kash.db")
    }
}
