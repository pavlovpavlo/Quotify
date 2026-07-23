package com.kovhan.feature.main.presentation.language

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
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.feature.main.presentation.profile.component.SettingsOptionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguageBottomSheet(
    selected: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(R.string.profile_language_sheet_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuotifyMaterialTheme.dimensions.space2),
        ) {
            SettingsOptionRow(
                label = stringResource(R.string.profile_language_uk),
                selected = selected == AppLanguage.UKRAINIAN,
                onClick = { onLanguageSelected(AppLanguage.UKRAINIAN) },
            )
            SettingsOptionRow(
                label = stringResource(R.string.profile_language_en),
                selected = selected == AppLanguage.ENGLISH,
                onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
            )
        }
    }
}
