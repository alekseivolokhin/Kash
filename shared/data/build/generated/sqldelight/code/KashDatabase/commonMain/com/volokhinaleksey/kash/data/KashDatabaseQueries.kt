package com.volokhinaleksey.kash.`data`

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class KashDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAllCategories(mapper: (
    id: Long,
    name: String,
    icon: String,
    color: Long,
    is_custom: Long,
  ) -> T): Query<T> = Query(-1_697_065_909, arrayOf("CategoryEntity"), driver, "KashDatabase.sq",
      "getAllCategories",
      "SELECT CategoryEntity.id, CategoryEntity.name, CategoryEntity.icon, CategoryEntity.color, CategoryEntity.is_custom FROM CategoryEntity") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun getAllCategories(): Query<CategoryEntity> = getAllCategories { id, name, icon, color,
      is_custom ->
    CategoryEntity(
      id,
      name,
      icon,
      color,
      is_custom
    )
  }

  public fun <T : Any> getCategoryById(id: Long, mapper: (
    id: Long,
    name: String,
    icon: String,
    color: Long,
    is_custom: Long,
  ) -> T): Query<T> = GetCategoryByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun getCategoryById(id: Long): Query<CategoryEntity> = getCategoryById(id) { id_, name,
      icon, color, is_custom ->
    CategoryEntity(
      id_,
      name,
      icon,
      color,
      is_custom
    )
  }

  public fun <T : Any> getAllTransactions(mapper: (
    id: Long,
    amount: Double,
    type: String,
    category_id: Long,
    date: Long,
    comment: String,
  ) -> T): Query<T> = Query(-1_545_582_652, arrayOf("TransactionEntity"), driver, "KashDatabase.sq",
      "getAllTransactions",
      "SELECT TransactionEntity.id, TransactionEntity.amount, TransactionEntity.type, TransactionEntity.category_id, TransactionEntity.date, TransactionEntity.comment FROM TransactionEntity ORDER BY date DESC") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getDouble(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun getAllTransactions(): Query<TransactionEntity> = getAllTransactions { id, amount, type,
      category_id, date, comment ->
    TransactionEntity(
      id,
      amount,
      type,
      category_id,
      date,
      comment
    )
  }

  public fun <T : Any> getTransactionById(id: Long, mapper: (
    id: Long,
    amount: Double,
    type: String,
    category_id: Long,
    date: Long,
    comment: String,
  ) -> T): Query<T> = GetTransactionByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getDouble(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun getTransactionById(id: Long): Query<TransactionEntity> = getTransactionById(id) { id_,
      amount, type, category_id, date, comment ->
    TransactionEntity(
      id_,
      amount,
      type,
      category_id,
      date,
      comment
    )
  }

  public fun insertCategory(
    name: String,
    icon: String,
    color: Long,
    is_custom: Long,
  ) {
    driver.execute(-245_729_989,
        """INSERT INTO CategoryEntity(name, icon, color, is_custom) VALUES (?, ?, ?, ?)""", 4) {
          bindString(0, name)
          bindString(1, icon)
          bindLong(2, color)
          bindLong(3, is_custom)
        }
    notifyQueries(-245_729_989) { emit ->
      emit("CategoryEntity")
    }
  }

  public fun deleteCategory(id: Long) {
    driver.execute(673_130_029, """DELETE FROM CategoryEntity WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(673_130_029) { emit ->
      emit("CategoryEntity")
    }
  }

  public fun insertTransaction(
    amount: Double,
    type: String,
    category_id: Long,
    date: Long,
    comment: String,
  ) {
    driver.execute(-1_314_269_823,
        """INSERT INTO TransactionEntity(amount, type, category_id, date, comment) VALUES (?, ?, ?, ?, ?)""",
        5) {
          bindDouble(0, amount)
          bindString(1, type)
          bindLong(2, category_id)
          bindLong(3, date)
          bindString(4, comment)
        }
    notifyQueries(-1_314_269_823) { emit ->
      emit("TransactionEntity")
    }
  }

  public fun updateTransaction(
    amount: Double,
    type: String,
    category_id: Long,
    date: Long,
    comment: String,
    id: Long,
  ) {
    driver.execute(-619_776_655,
        """UPDATE TransactionEntity SET amount = ?, type = ?, category_id = ?, date = ?, comment = ? WHERE id = ?""",
        6) {
          bindDouble(0, amount)
          bindString(1, type)
          bindLong(2, category_id)
          bindLong(3, date)
          bindString(4, comment)
          bindLong(5, id)
        }
    notifyQueries(-619_776_655) { emit ->
      emit("TransactionEntity")
    }
  }

  public fun deleteTransaction(id: Long) {
    driver.execute(617_949_007, """DELETE FROM TransactionEntity WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(617_949_007) { emit ->
      emit("TransactionEntity")
    }
  }

  private inner class GetCategoryByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("CategoryEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("CategoryEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-903_650_686,
        """SELECT CategoryEntity.id, CategoryEntity.name, CategoryEntity.icon, CategoryEntity.color, CategoryEntity.is_custom FROM CategoryEntity WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "KashDatabase.sq:getCategoryById"
  }

  private inner class GetTransactionByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TransactionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TransactionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(2_078_301_694,
        """SELECT TransactionEntity.id, TransactionEntity.amount, TransactionEntity.type, TransactionEntity.category_id, TransactionEntity.date, TransactionEntity.comment FROM TransactionEntity WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "KashDatabase.sq:getTransactionById"
  }
}
