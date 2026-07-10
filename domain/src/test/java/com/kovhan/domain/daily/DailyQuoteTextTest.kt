package com.kovhan.domain.daily

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.domain.settings.AppLanguage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DailyQuote.localizedText")
class DailyQuoteTextTest {

    private val both = DailyQuote(id = "d1", textEn = "Know thyself", textUk = "Пізнай себе")

    @Test
    @DisplayName("returns the Ukrainian text for the Ukrainian language")
    fun ukrainian() {
        assertEquals("Пізнай себе", both.localizedText(AppLanguage.UKRAINIAN))
    }

    @Test
    @DisplayName("returns the English text for the English language")
    fun english() {
        assertEquals("Know thyself", both.localizedText(AppLanguage.ENGLISH))
    }

    @Test
    @DisplayName("falls back to English when the Ukrainian text is blank")
    fun fallsBackToEnglish() {
        val quote = DailyQuote(id = "d1", textEn = "Know thyself", textUk = "")
        assertEquals("Know thyself", quote.localizedText(AppLanguage.UKRAINIAN))
    }

    @Test
    @DisplayName("falls back to Ukrainian when the English text is blank")
    fun fallsBackToUkrainian() {
        val quote = DailyQuote(id = "d1", textEn = "", textUk = "Пізнай себе")
        assertEquals("Пізнай себе", quote.localizedText(AppLanguage.ENGLISH))
    }

    @Test
    @DisplayName("localizes author and book per language with fallback")
    fun localizesAuthorAndBook() {
        val quote = DailyQuote(
            id = "d1",
            textEn = "en",
            textUk = "uk",
            authorEn = "Jane Austen",
            authorUk = "Джейн Остін",
            bookEn = "Pride and Prejudice",
            bookUk = null,
        )
        assertEquals("Джейн Остін", quote.localizedAuthor(AppLanguage.UKRAINIAN))
        assertEquals("Jane Austen", quote.localizedAuthor(AppLanguage.ENGLISH))
        assertEquals("Pride and Prejudice", quote.localizedBook(AppLanguage.UKRAINIAN))
        assertEquals("Pride and Prejudice", quote.localizedBook(AppLanguage.ENGLISH))
    }

    @Test
    @DisplayName("returns null author when neither language is present")
    fun nullAuthorWhenAbsent() {
        val quote = DailyQuote(id = "d1", textEn = "en", textUk = "uk")
        assertEquals(null, quote.localizedAuthor(AppLanguage.UKRAINIAN))
        assertEquals(null, quote.localizedBook(AppLanguage.ENGLISH))
    }
}
