package com.kovhan.feature.addquote.presentation.new_collection.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NewCollectionSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.addquote.presentation.new_collection.NewCollectionSheet
import com.kovhan.feature.addquote.presentation.new_collection.NewCollectionViewModel
import com.kovhan.feature.addquote.presentation.new_collection.mvi.NewCollectionEffect
import kotlinx.coroutines.launch

@Composable
internal fun NewCollectionSheetEntry(
    key: NewCollectionSheetKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<NewCollectionViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is NewCollectionEffect.Saved -> {
                    scope.launch {
                        coordinator.emitResult(
                            NavigationCoordinator.KEY_COLLECTION_CREATED,
                            effect.collectionId,
                        )
                        coordinator.dismissBottomSheet()
                    }
                }
            }
        }
    }

    NewCollectionSheet(
        name = state.value.name,
        isSaving = state.value.isSaving,
        onNameChange = viewModel::onNameChanged,
        onSave = viewModel::saveCollection,
        onBack = coordinator::dismissBottomSheet,
        onClose = coordinator::clearBottomSheets,
    )
}
