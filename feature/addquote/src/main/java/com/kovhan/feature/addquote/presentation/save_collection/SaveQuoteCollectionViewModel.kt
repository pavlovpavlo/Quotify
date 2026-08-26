package com.kovhan.feature.addquote.presentation.save_collection

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.collection.GetCollectionsUseCase
import com.kovhan.feature.addquote.presentation.common.QuoteSaveResult
import com.kovhan.feature.addquote.presentation.common.QuoteSaver
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft
import com.kovhan.feature.addquote.presentation.save_collection.mvi.SaveQuoteCollectionEffect
import com.kovhan.feature.addquote.presentation.save_collection.mvi.SaveQuoteCollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveQuoteCollectionViewModel @Inject constructor(
    private val getCollections: GetCollectionsUseCase,
    private val quoteSaver: QuoteSaver,
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

    fun onSaveWidgetPlaced(placed: Boolean) {
        widgetPlaced = placed
    }

    fun onChooseCollection(collectionId: String) = publishState {
        copy(chosenCollectionId = collectionId)
    }

    fun saveToCollection(draft: QuoteDraft, collectionId: String, generalName: String) {
        if (uiState.value.savingCollectionId != null) return
        viewModelScope.launch {
            publishState { copy(savingCollectionId = collectionId) }
            val result = quoteSaver.save(
                draft = draft,
                collectionId = collectionId,
                generalName = generalName,
                widgetPlaced = widgetPlaced,
            )
            when (result) {
                QuoteSaveResult.Blocked -> {
                    publishState { copy(savingCollectionId = null) }
                    publishEffect(SaveQuoteCollectionEffect.ShowPaywall)
                }

                is QuoteSaveResult.Saved ->
                    publishEffect(SaveQuoteCollectionEffect.Saved(result.action))
            }
        }
    }
}
