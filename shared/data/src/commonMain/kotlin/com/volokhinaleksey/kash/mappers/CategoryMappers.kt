package com.volokhinaleksey.kash.mappers

import com.volokhinaleksey.kash.data.CategoryEntity
import com.volokhinaleksey.kash.domain.model.Category

internal fun CategoryEntity.asDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
        isCustom = is_custom == 1L
    )
}