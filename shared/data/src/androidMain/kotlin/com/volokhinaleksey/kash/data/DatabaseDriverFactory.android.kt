package com.volokhinaleksey.kash.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.volokhinaleksey.kash.data.db.KashDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(KashDatabase.Schema, context, "kash.db")
    }
}
