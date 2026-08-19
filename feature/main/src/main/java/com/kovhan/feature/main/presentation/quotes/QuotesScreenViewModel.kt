package com.kovhan.feature.main.presentation.quotes

import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.activity.InAppReviewUseCase
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
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.LimitReason
import com.kovhan.core.analytics.event.LimitReached
import com.kovhan.core.analytics.DailyQuoteResult
import com.kovhan.core.analytics.event.DailyQuoteSaved
import com.kovhan.core.analytics.event.HideDailyQuoteFinished
import com.kovhan.core.analytics.event.HideDailyQuoteInitiated
import com.kovhan.core.analytics.event.RateUsInitiated
import com.kovhan.core.analytics.event.VisitLibrary
import com.kovhan.domain.premium.use_case.CheckQuoteLimitUseCase
import com.kovhan.domain.review.use_case.MarkReviewAskedUseCase
import com.kovhan.domain.review.use_case.ShouldAskForReviewUseCase
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
    private val checkQuoteLimit: CheckQuoteLimitUseCase,
    private val analytics: AnalyticsTracker,
    private val shouldAskForReview: ShouldAskForReviewUseCase,
    private val markReviewAsked: MarkReviewAskedUseCase,
    private val inAppReview: InAppReviewUseCase,
) : BaseViewModel<QuotesScreenState, QuotesScreenEffect>(QuotesScreenState()),
    QuotesScreenIntent {

    init {
        analytics.track(VisitLibrary)

        viewModelScope.launch {
            if (!shouldAskForReview()) return@launch
            markReviewAsked()
            analytics.track(RateUsInitiated)
            inAppReview()
        }

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

    fun onDailyQuoteHideRequested() {
        val quote = uiState.value.dailyQuote ?: return
        analytics.track(HideDailyQuoteInitiated(quote.id))
    }

    override fun onToggleDailyQuoteFavourite(favouritesName: String) {
        val quote = uiState.value.dailyQuote ?: return
        val language = uiState.value.language
        val wasFavourite = uiState.value.isDailyQuoteFavourite
        publishState { copy(isDailyQuoteFavouriteLoading = true) }
        viewModelScope.launch {
            // Додавання в обране створює нову цитату, тому впирається в той самий ліміт.
            if (!wasFavourite && !checkQuoteLimit()) {
                publishState { copy(isDailyQuoteFavouriteLoading = false) }
                analytics.track(LimitReached(LimitReason.QUOTES))
                publishEffect(QuotesScreenEffect.OpenPaywall)
                return@launch
            }
            val success = runCatching {
                if (wasFavourite) removeFromFavourites(quote)
                else saveToFavourites(quote, favouritesName, language)
            }.isSuccess
            if (success && !wasFavourite) analytics.track(DailyQuoteSaved(quote.id))
            publishState {
                copy(
                    isDailyQuoteFavourite = if (success) !wasFavourite else wasFavourite,
                    isDailyQuoteFavouriteLoading = false,
                )
            }
        }
    }

    override fun onHideDailyQuoteForever() {
        val quoteId = uiState.value.dailyQuote?.id.orEmpty()
        analytics.track(HideDailyQuoteFinished(quoteId, DailyQuoteResult.REMOVE))
        viewModelScope.launch { setDailyQuoteEnabled(false) }
    }

    override fun onHideDailyQuoteToday() {
        val quoteId = uiState.value.dailyQuote?.id.orEmpty()
        analytics.track(HideDailyQuoteFinished(quoteId, DailyQuoteResult.HIDE_FOR_TODAY))
        viewModelScope.launch {
            dismissForToday()
            publishState { copy(dailyQuote = null) }
        }
    }

    fun onDailyQuoteHideCancelled() {
        val quoteId = uiState.value.dailyQuote?.id.orEmpty()
        analytics.track(HideDailyQuoteFinished(quoteId, DailyQuoteResult.KEEP))
    }
}
