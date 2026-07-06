package com.kovhan.data.library.repository

import com.kovhan.core.models.Quote
import com.kovhan.core.models.QuoteFilter
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.remote.QuoteRemoteDataSource
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepositoryImpl @Inject constructor(
    private val remote: QuoteRemoteDataSource,
) : QuoteRepository {

    override suspend fun getAll(): List<Quote> =
        remote.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): Quote? =
        remote.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<Quote>> =
        remote.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeFiltered(filter: QuoteFilter): Flow<List<Quote>> =
        observeAll().map { quotes -> quotes.filter { it.matches(filter) } }

    override suspend fun deleteById(id: String) = remote.deleteById(id)

    override suspend fun edit(quote: Quote) = remote.edit(quote.toDto())

    override suspend fun setInPushPlaylist(id: String, added: Boolean) =
        remote.setInPushPlaylist(id, added)

    override suspend fun setInWidgetPlaylist(id: String, added: Boolean) =
        remote.setInWidgetPlaylist(id, added)
}
