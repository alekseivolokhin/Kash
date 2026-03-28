package com.volokhinaleksey.kash.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val color: Long,
    val isCustom: Boolean = false,
)
