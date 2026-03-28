package com.volokhinaleksey.kash.`data`

import kotlin.Long
import kotlin.String

public data class CategoryEntity(
  public val id: Long,
  public val name: String,
  public val icon: String,
  public val color: Long,
  public val is_custom: Long,
)
