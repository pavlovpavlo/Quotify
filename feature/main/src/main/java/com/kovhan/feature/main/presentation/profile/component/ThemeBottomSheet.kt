package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.domain.settings.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThemeBottomSheet(
    selected: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(R.string.profile_theme_sheet_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuotifyMaterialTheme.dimensions.space2),
        ) {
            SettingsOptionRow(
                label = stringResource(R.string.profile_theme_light),
                selected = selected == AppTheme.LIGHT,
                onClick = { onThemeSelected(AppTheme.LIGHT) },
            )
            SettingsOptionRow(
                label = stringResource(R.string.profile_theme_dark),
                selected = selected == AppTheme.DARK,
                onClick = { onThemeSelected(AppTheme.DARK) },
            )
            SettingsOptionRow(
                label = stringResource(R.string.profile_theme_system),
                selected = selected == AppTheme.SYSTEM,
                onClick = { onThemeSelected(AppTheme.SYSTEM) },
            )
        }
    }
}
