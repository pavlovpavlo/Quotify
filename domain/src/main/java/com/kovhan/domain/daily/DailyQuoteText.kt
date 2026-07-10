package com.kovhan.domain.daily

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.domain.settings.AppLanguage

fun DailyQuote.localizedText(language: AppLanguage): String = when (language) {
    AppLanguage.UKRAINIAN -> textUk.ifBlank { textEn }
    AppLanguage.ENGLISH -> textEn.ifBlank { textUk }
}

fun DailyQuote.localizedAuthor(language: AppLanguage): String? =
    pickLocalized(authorEn, authorUk, language)

fun DailyQuote.localizedBook(language: AppLanguage): String? =
    pickLocalized(bookEn, bookUk, language)

private fun pickLocalized(en: String?, uk: String?, language: AppLanguage): String? {
    val enValue = en?.takeUnless { it.isBlank() }
    val ukValue = uk?.takeUnless { it.isBlank() }
    return when (language) {
        AppLanguage.UKRAINIAN -> ukValue ?: enValue
        AppLanguage.ENGLISH -> enValue ?: ukValue
    }
}
