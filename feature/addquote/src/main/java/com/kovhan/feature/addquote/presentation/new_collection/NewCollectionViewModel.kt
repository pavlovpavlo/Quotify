package com.kovhan.feature.addquote.presentation.new_collection

import com.kovhan.core.models.SavedCollection
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.library.use_case.collection.EditCollectionUseCase
import com.kovhan.feature.addquote.presentation.new_collection.mvi.NewCollectionEffect
import com.kovhan.feature.addquote.presentation.new_collection.mvi.NewCollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCollectionViewModel @Inject constructor(
    private val editCollection: EditCollectionUseCase,
    private val idGenerator: IdGenerator,
) : BaseViewModel<NewCollectionState, NewCollectionEffect>(NewCollectionState()) {

    fun onNameChanged(value: String) = publishState { copy(name = value.take(40)) }

    fun saveCollection() {
        val state = uiState.value
        if (!state.canSave) return
        viewModelScope.launch {
            publishState { copy(isSaving = true) }
            val id = idGenerator.generate()
            editCollection(
                SavedCollection(
                    id = id,
                    name = state.name.trim(),
                ),
            )
            publishEffect(NewCollectionEffect.Saved(id))
        }
    }
}
