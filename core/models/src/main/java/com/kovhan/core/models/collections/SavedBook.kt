package com.kovhan.core.models.collections

data class SavedBook(
    val id: String,
    val name: String,
    val quoteCount: Int? = null,
)
