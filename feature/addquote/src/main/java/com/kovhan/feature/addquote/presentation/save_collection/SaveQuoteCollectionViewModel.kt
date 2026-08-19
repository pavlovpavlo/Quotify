package com.kovhan.feature.addquote.presentation.save_collection

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.collection.GetCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.SaveQuoteToCollectionUseCase
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.LimitReason
import com.kovhan.core.analytics.event.LimitReached
import com.kovhan.core.analytics.event.QuoteAdded
import com.kovhan.feature.addquote.presentation.analytics.toAnalytics
import com.kovhan.domain.premium.use_case.CheckQuoteLimitUseCase
import com.kovhan.domain.quote.CheckAddQuoteActionUseCase
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft
import com.kovhan.feature.addquote.presentation.save_collection.mvi.SaveQuoteCollectionEffect
import com.kovhan.feature.addquote.presentation.save_collection.mvi.SaveQuoteCollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveQuoteCollectionViewModel @Inject constructor(
    private val getCollections: GetCollectionsUseCase,
    private val saveQuoteToCollection: SaveQuoteToCollectionUseCase,
    private val checkAddQuoteActionUseCase: CheckAddQuoteActionUseCase,
    private val checkQuoteLimit: CheckQuoteLimitUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<SaveQuoteCollectionState, SaveQuoteCollectionEffect>(SaveQuoteCollectionState()) {

    private var widgetPlaced = false
    fun loadCollections(generalName: String, selectId: String? = null) {
        viewModelScope.launch {
            val collections = getCollections(withCount = true)
            val general = collections.firstOrNull { it.id == SavedCollection.GENERAL_ID }
                ?: SavedCollection(id = SavedCollection.GENERAL_ID, name = generalName)
            val ordered = listOf(general) + collections.filterNot {
                it.id == SavedCollection.GENERAL_ID || it.id == SavedCollection.FAVOURITES_ID
            }
            publishState {
                copy(
                    collections = ordered,
                    chosenCollectionId = selectId ?: chosenCollectionId ?: ordered.firstOrNull()?.id,
                )
            }
        }
    }

    fun onSaveWidgetPlaced(placed: Boolean){
        widgetPlaced = placed
    }

    fun onChooseCollection(collectionId: String) = publishState {
        copy(chosenCollectionId = collectionId)
    }

    fun saveToCollection(draft: QuoteDraft, collectionId: String, generalName: String) {
        if (uiState.value.savingCollectionId != null) return
        viewModelScope.launch {
            publishState { copy(savingCollectionId = collectionId) }
            if (!checkQuoteLimit()) {
                publishState { copy(savingCollectionId = null) }
                analytics.track(LimitReached(LimitReason.QUOTES))
                publishEffect(SaveQuoteCollectionEffect.ShowPaywall)
                return@launch
            }
            saveQuoteToCollection(
                text = draft.text,
                authorName = draft.authorName,
                bookName = draft.bookName,
                tagNames = draft.tagNames,
                collectionId = collectionId,
                inWidgetPlaylist = draft.inWidgetPlaylist,
                inPushPlaylist = draft.inPushPlaylist,
                generalName = generalName,
                page = draft.page,
            )
            analytics.track(
                QuoteAdded(
                    inputMethod = draft.inputMethod.toAnalytics(),
                    textLength = draft.text.trim().length,
                    hasAuthor = !draft.authorName.isNullOrBlank(),
                    hasBook = !draft.bookName.isNullOrBlank(),
                    tagsCount = draft.tagNames.size,
                    widgetEnabled = draft.inWidgetPlaylist,
                    pushEnabled = draft.inPushPlaylist,
                ),
            )
            publishEffect(SaveQuoteCollectionEffect.Saved(checkAddQuoteActionUseCase.invoke(widgetPlaced)))
        }
    }
}
