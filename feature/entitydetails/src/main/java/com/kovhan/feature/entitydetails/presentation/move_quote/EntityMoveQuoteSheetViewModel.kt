package com.kovhan.feature.entitydetails.presentation.move_quote

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.navigation.MoveQuoteSheetKey
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.feature.entitydetails.presentation.move_quote.mvi.EntityMoveQuoteSheetEffect
import com.kovhan.feature.entitydetails.presentation.move_quote.mvi.EntityMoveQuoteSheetIntent
import com.kovhan.feature.entitydetails.presentation.move_quote.mvi.EntityMoveQuoteSheetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EntityMoveQuoteSheetViewModel @Inject constructor(
    private val observeCollections: ObserveCollectionsUseCase,
) : BaseViewModel<EntityMoveQuoteSheetState, EntityMoveQuoteSheetEffect>(EntityMoveQuoteSheetState()),
    EntityMoveQuoteSheetIntent {

    private var bindJob: Job? = null
    private var excludedCollectionId: String? = null
    private var generalName: String = ""

    fun bind(key: MoveQuoteSheetKey, generalName: String) {
        excludedCollectionId = key.excludedCollectionId
        this.generalName = generalName
        bindJob?.cancel()

        publishState {
            copy(
                quoteId = key.quoteId,
                selectedCollectionId = key.selectedCollectionId,
            )
        }

        bindJob = observeCollections()
            .onEach { collections ->
                val targets = buildTargets(collections)
                publishState {
                    copy(
                        targets = targets,
                        selectedCollectionId = selectedCollectionId
                            ?: targets.firstOrNull()?.id,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onSelectCollection(collectionId: String) {
        publishState { copy(selectedCollectionId = collectionId) }
    }

    override fun onCreateCollectionRequested() {
        publishEffect(EntityMoveQuoteSheetEffect.OpenNewCollectionSheet)
    }

    override fun onCollectionCreated(collectionId: String) {
        publishState { copy(selectedCollectionId = collectionId) }
    }

    override fun onMoveRequested(targetCollectionId: String) {
        val quoteId = uiState.value.quoteId
        if (quoteId.isBlank()) return
        publishEffect(
            EntityMoveQuoteSheetEffect.CloseWithResult(
                quoteId = quoteId,
                targetCollectionId = targetCollectionId,
            ),
        )
    }

    private fun buildTargets(collections: List<SavedCollection>): List<SavedCollection> {
        val general = collections.firstOrNull { it.id == SavedCollection.GENERAL_ID }
            ?: SavedCollection(
                id = SavedCollection.GENERAL_ID,
                name = generalName,
            )

        return (listOf(general) + collections.filterNot { it.id == SavedCollection.GENERAL_ID })
            .distinctBy { it.id }
            .filterNot { it.id == SavedCollection.FAVOURITES_ID }
            .filterNot { it.id == excludedCollectionId }
    }
}
