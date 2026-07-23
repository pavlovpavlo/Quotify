package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.bottomsheet.BottomSheetHeight
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.addquote.presentation.details.mvi.AiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagSheet(
    tagQuery: String,
    aiState: AiState,
    aiTags: List<String>,
    recentTags: List<String>,
    canCreate: Boolean,
    selectedCount: Int,
    isSelected: (String) -> Boolean,
    onQueryChange: (String) -> Unit,
    onCreate: () -> Unit,
    onGenerate: () -> Unit,
    onToggle: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    QuotifyBottomSheet(
        onDismiss = onClose,
        modifier = modifier,
        sheetState = sheetState,
        height = BottomSheetHeight.WRAP_CONTENT,
        title = stringResource(R.string.details_tag_sheet_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space6),
        ) {
            TagSheetSearch(
                modifier = Modifier.fillMaxWidth(),
                query = tagQuery,
                onQueryChange = onQueryChange,
                onSubmit = { if (canCreate) onCreate() },
                placeholder = stringResource(R.string.details_tag_search_ph),
            )

            if (canCreate) {
                TagCreateButton(
                    modifier = Modifier.padding(top = dimensions.space3),
                    query = tagQuery.trim(),
                    onClick = onCreate,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = dimensions.space4),
                verticalArrangement = Arrangement.spacedBy(dimensions.space5),
            ) {
                AiTagsSection(
                    aiState = aiState,
                    tags = aiTags,
                    isSelected = isSelected,
                    onToggle = onToggle,
                    onGenerate = onGenerate,
                )
                RecentTagsSection(
                    tags = recentTags,
                    isSelected = isSelected,
                    onToggle = onToggle,
                )
            }

            QuotifyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space4),
                text = if (selectedCount > 0) {
                    stringResource(R.string.details_tag_done_count, selectedCount)
                } else {
                    stringResource(R.string.details_tag_done)
                },
                onClick = onClose,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }
    }
}
