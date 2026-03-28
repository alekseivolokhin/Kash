package com.volokhinaleksey.kash.`data`

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class TransactionEntity(
  public val id: Long,
  public val amount: Double,
  public val type: String,
  public val category_id: Long,
  public val date: Long,
  public val comment: String,
)
