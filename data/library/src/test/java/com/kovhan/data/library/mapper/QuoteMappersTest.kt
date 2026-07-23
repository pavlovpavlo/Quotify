package com.kovhan.data.library.mapper

import com.kovhan.core.models.quote.Quote
import com.kovhan.data.library.dto.QuoteDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("QuoteMappers")
class QuoteMappersTest {

    private val domain = Quote(
        id = "q1",
        text = "To be or not to be",
        authorId = "a1",
        bookId = "b1",
        tagIds = listOf("t1", "t2"),
        inPushPlaylist = true,
        inWidgetPlaylist = false,
        sourceDailyId = "d1",
    )

    private val dto = QuoteDto(
        id = "q1",
        text = "To be or not to be",
        authorId = "a1",
        bookId = "b1",
        tagIds = listOf("t1", "t2"),
        inPushPlaylist = true,
        inWidgetPlaylist = false,
        sourceDailyId = "d1",
    )

    @Test
    @DisplayName("maps every field from dto to domain")
    fun mapsDtoToDomain() {
        assertEquals(domain, dto.toDomain())
    }

    @Test
    @DisplayName("maps every field from domain to dto")
    fun mapsDomainToDto() {
        assertEquals(dto, domain.toDto())
    }

    @Test
    @DisplayName("round-trips without losing the tag list or playlist flags")
    fun roundTrips() {
        assertEquals(domain, domain.toDto().toDomain())
    }

    @Test
    @DisplayName("maps a bare dto to null references, no tags and both playlists off")
    fun mapsBareDto() {
        assertEquals(Quote(id = "q1", text = "bare"), QuoteDto(id = "q1", text = "bare").toDomain())
    }
}
