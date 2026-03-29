package com.volokhinaleksey.kash.mappers

import com.volokhinaleksey.kash.data.TransactionEntity
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.model.TransactionType

internal fun TransactionEntity.asDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf(type),
        categoryId = category_id,
        date = date,
        comment = comment,
    )
}