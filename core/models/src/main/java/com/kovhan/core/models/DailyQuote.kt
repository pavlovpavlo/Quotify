package com.kovhan.core.models

data class DailyQuote(
    val id: String,
    val textEn: String,
    val textUk: String,
    val authorEn: String? = null,
    val authorUk: String? = null,
    val bookEn: String? = null,
    val bookUk: String? = null,
)
