package com.kovhan.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.kovhan.design.systems.QuotifyMaterialTheme


@Composable
internal fun AuthScreenScaffold(
    onBack: () -> Unit,
    paddingValues: PaddingValues,
    scrollable: Boolean = true,
    horizontalPadding: Dp = QuotifyMaterialTheme.dimensions.space6,
    topPadding: Dp = QuotifyMaterialTheme.dimensions.space1,
    bottomPadding: Dp = QuotifyMaterialTheme.dimensions.size18,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    body: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuotifyMaterialTheme.colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AuthTopBar(onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(
                        if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier,
                    )
                    .padding(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = topPadding,
                        bottom = bottomPadding,
                    )
                    .imePadding(),
                content = body,
            )

            if (footer != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            bottom = bottomPadding,
                        ),
                    content = footer,
                )
            }
        }
    }
}
