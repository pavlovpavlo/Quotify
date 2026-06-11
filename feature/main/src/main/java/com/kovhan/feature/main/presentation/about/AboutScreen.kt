package com.kovhan.feature.main.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.core.ui.util.appVersionName
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.about.navigation.AboutScreenNavAction

@Composable
fun AboutScreen(
    navAction: AboutScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val version = LocalContext.current.appVersionName()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        QuotifyTopBar(
            title = stringResource(R.string.about_title),
            onBack = navAction::navigateBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.about_placeholder),
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                style = QuotifyMaterialTheme.typography.body,
            )
            if (version.isNotBlank()) {
                Text(
                    text = stringResource(R.string.profile_version, version),
                    color = colors.textTertiary,
                    textAlign = TextAlign.Center,
                    style = QuotifyMaterialTheme.typography.caption,
                )
            }
        }
    }
}
