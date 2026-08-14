package com.kovhan.domain.library

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.quote.QuoteFilter
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    suspend fun getAll(): List<Quote>

    /** Кількість збережених цитат — для ліміту безкоштовного плану. */
    suspend fun count(): Int

    suspend fun getById(id: String): Quote?

    fun observeAll(): Flow<List<Quote>>

    fun observeFiltered(filter: QuoteFilter): Flow<List<Quote>>

    suspend fun deleteById(id: String)

    suspend fun edit(quote: Quote)

    suspend fun setInPushPlaylist(id: String, added: Boolean)

    suspend fun setInWidgetPlaylist(id: String, added: Boolean)
}
