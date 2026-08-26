package com.kovhan.feature.main.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.core.ui.util.appVersionName
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.about.model.AboutBlock
import com.kovhan.feature.main.presentation.about.model.aboutSections
import com.kovhan.feature.main.presentation.about.navigation.AboutScreenNavAction

private const val BULLET = "•"

@Composable
fun AboutScreen(
    navAction: AboutScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography
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
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = paddingValues.calculateBottomPadding() + 32.dp,
                ),
        ) {
            aboutSections.forEachIndexed { sectionIndex, section ->
                if (section.title != null) {
                    Spacer(Modifier.height(if (sectionIndex == 0) 8.dp else 28.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = stringResource(section.title),
                        color = colors.textPrimary,
                        style = typography.h4,
                    )
                } else {
                    Spacer(Modifier.height(if (sectionIndex == 0) 8.dp else 28.dp))
                }

                section.blocks.forEachIndexed { blockIndex, block ->
                    if (blockIndex > 0) Spacer(Modifier.height(12.dp))
                    when (block) {
                        is AboutBlock.Paragraph -> Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(block.text),
                            color = colors.textSecondary,
                            style = typography.readingBody,
                        )

                        is AboutBlock.Bullets -> Column(modifier = Modifier.fillMaxWidth()) {
                            block.items.forEachIndexed { itemIndex, item ->
                                if (itemIndex > 0) Spacer(Modifier.height(8.dp))
                                BulletRow(text = stringResource(item))
                            }
                        }
                    }
                }
            }

            if (version.isNotBlank()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.profile_version, version),
                    color = colors.textTertiary,
                    style = typography.caption,
                )
            }
        }
    }
}

@Composable
private fun BulletRow(text: String) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = BULLET,
            color = colors.accentPrimary,
            style = typography.readingBody.copy(fontWeight = FontWeight.W600),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            color = colors.textSecondary,
            style = typography.readingBody,
        )
    }
}
