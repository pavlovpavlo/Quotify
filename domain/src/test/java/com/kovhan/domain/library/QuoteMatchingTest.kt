package com.kovhan.domain.library

import com.kovhan.core.models.Quote
import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.QuotePlaylist
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Quote.matches")
class QuoteMatchingTest {

    private val quote = Quote(
        id = "q1",
        text = "x",
        authorId = "a1",
        bookId = "b1",
        tagIds = listOf("t1", "t2"),
        inPushPlaylist = true,
        inWidgetPlaylist = false,
    )

    @Test
    @DisplayName("an empty filter matches any quote")
    fun emptyFilterMatches() {
        assertTrue(quote.matches(QuoteFilter()))
    }

    @Test
    @DisplayName("matches by author id")
    fun matchesByAuthor() {
        assertTrue(quote.matches(QuoteFilter(authorId = "a1")))
        assertFalse(quote.matches(QuoteFilter(authorId = "other")))
    }

    @Test
    @DisplayName("matches by book id")
    fun matchesByBook() {
        assertTrue(quote.matches(QuoteFilter(bookId = "b1")))
        assertFalse(quote.matches(QuoteFilter(bookId = "other")))
    }

    @Test
    @DisplayName("matches by tag membership")
    fun matchesByTag() {
        assertTrue(quote.matches(QuoteFilter(tagId = "t2")))
        assertFalse(quote.matches(QuoteFilter(tagId = "tX")))
    }

    @Test
    @DisplayName("matches by playlist membership")
    fun matchesByPlaylist() {
        assertTrue(quote.matches(QuoteFilter(playlist = QuotePlaylist.PUSH)))
        assertFalse(quote.matches(QuoteFilter(playlist = QuotePlaylist.WIDGET)))
    }

    @Test
    @DisplayName("combines several criteria with AND")
    fun combinesWithAnd() {
        assertTrue(quote.matches(QuoteFilter(authorId = "a1", playlist = QuotePlaylist.PUSH)))
        assertFalse(quote.matches(QuoteFilter(authorId = "a1", playlist = QuotePlaylist.WIDGET)))
    }
}
