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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.snackbar.QuotifySnackbar
import com.kovhan.design.systems.QuotifyMaterialTheme

private val AuthBodyHorizontal = 24.dp
private val AuthBodyTop = 4.dp
private val AuthBodyBottom = 18.dp

@Composable
internal fun AuthScreenScaffold(
    onBack: () -> Unit,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollable: Boolean = true,
    body: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuotifyMaterialTheme.colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
            .imePadding(),
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
                        start = AuthBodyHorizontal,
                        end = AuthBodyHorizontal,
                        top = AuthBodyTop,
                        bottom = AuthBodyBottom,
                    ),
                content = body,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
        ) { data ->
            QuotifySnackbar(snackbarData = data)
        }
    }
}
