package com.kovhan.data.library.mapper

import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.data.library.dto.CollectionDto
import com.kovhan.data.library.dto.SavedAuthorDto
import com.kovhan.data.library.dto.SavedBookDto
import com.kovhan.data.library.dto.SavedTagDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("LibraryMappers")
class LibraryMappersTest {

    @Nested
    @DisplayName("author")
    inner class Author {

        @Test
        @DisplayName("maps between dto and domain")
        fun mapsBothWays() {
            assertEquals(SavedAuthor("a1", "Camus"), SavedAuthorDto("a1", "Camus").toDomain())
            assertEquals(SavedAuthorDto("a1", "Camus"), SavedAuthor("a1", "Camus").toDto())
        }
    }

    @Nested
    @DisplayName("tag")
    inner class Tag {

        @Test
        @DisplayName("maps between dto and domain")
        fun mapsBothWays() {
            assertEquals(SavedTag("t1", "Absurdism"), SavedTagDto("t1", "Absurdism").toDomain())
            assertEquals(SavedTagDto("t1", "Absurdism"), SavedTag("t1", "Absurdism").toDto())
        }
    }

    @Nested
    @DisplayName("book")
    inner class Book {

        @Test
        @DisplayName("maps between dto and domain")
        fun mapsBothWays() {
            assertEquals(SavedBook("b1", "The Stranger"), SavedBookDto("b1", "The Stranger").toDomain())
            assertEquals(SavedBookDto("b1", "The Stranger"), SavedBook("b1", "The Stranger").toDto())
        }
    }

    @Nested
    @DisplayName("collection")
    inner class Collection {

        @Test
        @DisplayName("maps every field from dto to domain")
        fun mapsDtoToDomain() {
            val dto = CollectionDto("c1", "Stoics", iconId = "scroll", iconColor = "112233")

            assertEquals(SavedCollection("c1", "Stoics", "scroll", "112233"), dto.toDomain())
        }

        @Test
        @DisplayName("round-trips without swapping icon id and color")
        fun roundTrips() {
            val collection = SavedCollection("c1", "Stoics", iconId = "scroll", iconColor = "112233")

            assertEquals(collection, collection.toDto().toDomain())
        }

        @Test
        @DisplayName("carries the default icon and color from a bare dto")
        fun carriesDefaults() {
            assertEquals(SavedCollection("c1", "Stoics"), CollectionDto("c1", "Stoics").toDomain())
        }
    }
}
