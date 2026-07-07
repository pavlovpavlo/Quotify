package com.kovhan.data.library.mapper

import com.kovhan.core.models.DailyQuote
import com.kovhan.data.library.dto.DailyQuoteDto
import com.kovhan.data.library.local.daily.DailyQuoteEntity

fun DailyQuoteDto.toEntity() = DailyQuoteEntity(
    id = id,
    textEn = textEn,
    textUk = textUk,
    authorEn = authorEn,
    authorUk = authorUk,
    bookEn = bookEn,
    bookUk = bookUk,
)

fun DailyQuoteEntity.toDomain() = DailyQuote(
    id = id,
    textEn = textEn,
    textUk = textUk,
    authorEn = authorEn,
    authorUk = authorUk,
    bookEn = bookEn,
    bookUk = bookUk,
)
