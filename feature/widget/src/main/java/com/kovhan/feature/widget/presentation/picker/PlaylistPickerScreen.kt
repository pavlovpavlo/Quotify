package com.kovhan.feature.widget.presentation.picker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.widget.presentation.picker.component.PlaylistPickResults
import com.kovhan.feature.widget.presentation.picker.component.PlaylistPickerFooter
import com.kovhan.feature.widget.presentation.picker.component.PlaylistPickerTopBar
import com.kovhan.feature.widget.presentation.picker.component.PlaylistScopesRow
import com.kovhan.feature.widget.presentation.picker.component.PlaylistSearchField
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerIntent
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerState

@Composable
internal fun PlaylistPickerScreen(
    state: PlaylistPickerState,
    intent: PlaylistPickerIntent,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors

    BackHandler(enabled = state.menuVisible) { intent.onDismissMenu() }
    BackHandler(enabled = !state.menuVisible, onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
            return@Box
        }

        Column(modifier = Modifier.fillMaxSize()) {
            PlaylistPickerTopBar(state = state, intent = intent, onBack = onBack)

            PlaylistSearchField(
                query = state.query,
                onQueryChange = intent::onQueryChange,
                onClear = { intent.onQueryChange("") },
            )

            PlaylistScopesRow(
                scope = state.scope,
                onScopeChange = intent::onScopeChange,
                results = state.results,
            )

            PlaylistPickResults(
                scope = state.scope,
                query = state.query.trim(),
                results = state.results,
                picked = state.picked,
                onToggle = intent::onTogglePick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )

            PlaylistPickerFooter(
                enabled = state.canSave,
                onSave = intent::onSaveClicked,
            )
        }
    }
}
