package com.kovhan.feature.addquote.presentation.details.component.tageditor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.TagSheetResult
import com.kovhan.core.navigation.TagSheetAiLimitDialogKey
import com.kovhan.core.navigation.TagSheetKey
import com.kovhan.core.navigation.TagSheetOfflineDialogKey
import com.kovhan.feature.addquote.presentation.common.toNav
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagSheet
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagSheetEffect
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagSheetViewModel
import kotlinx.coroutines.launch

@Composable
internal fun TagSheetEntry(
    key: TagSheetKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<TagSheetViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key) {
        viewModel.initialize(
            quoteText = key.quoteText,
            selectedTags = key.selectedTags,
            tagPool = key.tagPool,
            aiTags = key.aiTags,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is TagSheetEffect.ShowAiLimitDialog -> {
                    coordinator.showDialog(
                        TagSheetAiLimitDialogKey(reason = effect.reason.toNav()),
                    )
                }

                TagSheetEffect.ShowOfflineDialog -> {
                    coordinator.showDialog(TagSheetOfflineDialogKey)
                }

                TagSheetEffect.OpenPaywall -> coordinator.navigate(PaywallKey)
            }
        }
    }

    fun dismissWithResult() {
        scope.launch {
            val result = viewModel.buildResult()
            coordinator.emitResult(
                NavigationCoordinator.KEY_TAG_SHEET_RESULT,
                TagSheetResult(
                    selectedTags = result.selectedTags,
                    aiTags = result.aiTags,
                ),
            )
            coordinator.dismissBottomSheet()
        }
    }

    TagSheet(
        tagQuery = state.value.tagQuery,
        aiState = state.value.aiState,
        aiTags = state.value.filteredAiTags,
        aiLocked = !state.value.isPremium,
        recentTags = state.value.recentTags,
        canCreate = state.value.canCreateTag,
        selectedCount = state.value.selectedTags.size,
        isSelected = state.value::isTagSelected,
        onQueryChange = viewModel::onQueryChange,
        onCreate = viewModel::onCreateTag,
        onGenerate = viewModel::onGenerateAiTags,
        onToggle = viewModel::onToggleTag,
        onClose = ::dismissWithResult,
    )
}
