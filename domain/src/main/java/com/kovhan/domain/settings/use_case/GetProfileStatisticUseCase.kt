package com.kovhan.domain.settings.use_case

import com.kovhan.core.models.profile.ProfileStatistic
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.ObserveQuotesUseCase
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileStatisticUseCase @Inject constructor(
    val observeCollections: ObserveCollectionsUseCase,
    val observeSavedBooks: ObserveSavedBooksUseCase,
    val observeSavedAuthors: ObserveSavedAuthorsUseCase,
    val observeQuotesUseCase: ObserveQuotesUseCase
) {
    operator fun invoke(): Flow<ProfileStatistic> = combine(
        observeQuotesUseCase(),
        observeCollections(),
        observeSavedBooks(),
        observeSavedAuthors()
    ) { quotes, folders, books, authors ->
        ProfileStatistic(
            countOfQuotes = quotes.size,
            countOfBooks = books.size,
            countOfAuthors = authors.size,
            countOfCollections = folders.size
        )
    }
}
