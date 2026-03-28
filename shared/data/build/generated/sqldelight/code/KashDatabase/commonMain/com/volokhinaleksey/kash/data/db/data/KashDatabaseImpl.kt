package com.volokhinaleksey.kash.`data`.db.`data`

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.volokhinaleksey.kash.`data`.KashDatabaseQueries
import com.volokhinaleksey.kash.`data`.db.KashDatabase
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<KashDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = KashDatabaseImpl.Schema

internal fun KClass<KashDatabase>.newInstance(driver: SqlDriver): KashDatabase =
    KashDatabaseImpl(driver)

private class KashDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), KashDatabase {
  override val kashDatabaseQueries: KashDatabaseQueries = KashDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE CategoryEntity (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    name TEXT NOT NULL,
          |    icon TEXT NOT NULL,
          |    color INTEGER NOT NULL,
          |    is_custom INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE TransactionEntity (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    amount REAL NOT NULL,
          |    type TEXT NOT NULL,
          |    category_id INTEGER NOT NULL,
          |    date INTEGER NOT NULL,
          |    comment TEXT NOT NULL DEFAULT '',
          |    FOREIGN KEY (category_id) REFERENCES CategoryEntity(id)
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
