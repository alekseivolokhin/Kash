package com.volokhinaleksey.kash.`data`.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.volokhinaleksey.kash.`data`.KashDatabaseQueries
import com.volokhinaleksey.kash.`data`.db.`data`.newInstance
import com.volokhinaleksey.kash.`data`.db.`data`.schema
import kotlin.Unit

public interface KashDatabase : Transacter {
  public val kashDatabaseQueries: KashDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = KashDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): KashDatabase =
        KashDatabase::class.newInstance(driver)
  }
}
