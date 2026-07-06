package com.kovhan.core.models

data class SavedAuthor(
    val id: String,
    val name: String,
    val quoteCount: Int? = null,
)
