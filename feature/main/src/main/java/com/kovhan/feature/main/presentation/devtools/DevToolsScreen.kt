package com.kovhan.feature.main.presentation.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.devtools.component.DevDialogPicker
import com.kovhan.feature.main.presentation.profile.component.ProfileSectionTitle

@Composable
fun DevToolsScreen(
    onBack: () -> Unit,
    onOpenOffer: () -> Unit,
    onOpenDialog: (DialogKey) -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        QuotifyTopBar(
            title = stringResource(R.string.dev_tools_title),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.dev_tools_open_offer),
                onClick = onOpenOffer,
                variant = QuotifyButtonVariant.Outlined,
                accent = QuotifyButtonAccent.Ai,
                size = QuotifyButtonSize.Medium,
            )

            VerticalSpacer(dimensions.space6)

            ProfileSectionTitle(stringResource(R.string.dev_tools_dialogs_section))

            DevDialogPicker(
                dialogs = DevDialogCatalog,
                onDialogClick = onOpenDialog,
            )

            VerticalSpacer(dimensions.space3)
        }
    }
}
