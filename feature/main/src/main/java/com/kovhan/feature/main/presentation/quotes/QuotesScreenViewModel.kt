package com.kovhan.feature.main.presentation.quotes

import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.daily.localizedAuthor
import com.kovhan.domain.daily.localizedBook
import com.kovhan.domain.daily.localizedText
import com.kovhan.domain.daily.use_case.DismissDailyQuoteForTodayUseCase
import com.kovhan.domain.daily.use_case.GetDailyQuoteUseCase
import com.kovhan.domain.daily.use_case.RemoveDailyQuoteFromFavouritesUseCase
import com.kovhan.domain.daily.use_case.SaveDailyQuoteToFavouritesUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.ObserveFilteredQuotesUseCase
import com.kovhan.domain.settings.use_case.GetDailyQuoteEnabledUseCase
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.SetDailyQuoteEnabledUseCase
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenEffect
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenIntent
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesScreenViewModel @Inject constructor(
    private val getDailyQuoteEnabled: GetDailyQuoteEnabledUseCase,
    private val getDailyQuote: GetDailyQuoteUseCase,
    private val getLanguage: GetLanguageUseCase,
    private val observeFavourites: ObserveFilteredQuotesUseCase,
    private val observeCollections: ObserveCollectionsUseCase,
    private val saveToFavourites: SaveDailyQuoteToFavouritesUseCase,
    private val removeFromFavourites: RemoveDailyQuoteFromFavouritesUseCase,
    private val dismissForToday: DismissDailyQuoteForTodayUseCase,
    private val setDailyQuoteEnabled: SetDailyQuoteEnabledUseCase,
) : BaseViewModel<QuotesScreenState, QuotesScreenEffect>(QuotesScreenState()),
    QuotesScreenIntent {

    init {
        observeCollections()
            .onEach { collections ->
                publishState {
                    copy(folders = collections, areFoldersLoading = false)
                }
            }
            .launchIn(viewModelScope)
        combine(
            getDailyQuoteEnabled(),
            getLanguage(),
            observeFavourites(QuoteFilter(collectionId = SavedCollection.FAVOURITES_ID)),
        ) { enabled, language, favourites ->
            val quote = if (enabled) getDailyQuote() else null
            Triple(quote, language, favourites)
        }.onEach { (quote, language, favourites) ->
                publishState {
                    copy(
                        isDailyQuoteLoading = false,
                        dailyQuote = quote,
                        language = language,
                        displayText = quote?.localizedText(language).orEmpty(),
                        displayAuthor = quote?.localizedAuthor(language),
                        displayBook = quote?.localizedBook(language),
                        isDailyQuoteFavourite = if (isDailyQuoteFavouriteLoading) {
                            isDailyQuoteFavourite
                        } else {
                            quote != null && favourites.any { it.sourceDailyId == quote.id }
                        },
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onToggleDailyQuoteFavourite(favouritesName: String) {
        val quote = uiState.value.dailyQuote ?: return
        val language = uiState.value.language
        val wasFavourite = uiState.value.isDailyQuoteFavourite
        publishState { copy(isDailyQuoteFavouriteLoading = true) }
        viewModelScope.launch {
            val success = runCatching {
                if (wasFavourite) removeFromFavourites(quote)
                else saveToFavourites(quote, favouritesName, language)
            }.isSuccess
            publishState {
                copy(
                    isDailyQuoteFavourite = if (success) !wasFavourite else wasFavourite,
                    isDailyQuoteFavouriteLoading = false,
                )
            }
        }
    }

    override fun onHideDailyQuoteForever() {
        viewModelScope.launch { setDailyQuoteEnabled(false) }
    }

    override fun onHideDailyQuoteToday() {
        viewModelScope.launch {
            dismissForToday()
            publishState { copy(dailyQuote = null) }
        }
    }
}
