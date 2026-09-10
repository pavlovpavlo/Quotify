package com.kovhan.feature.widget.presentation.quote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.emptystate.DefaultEmptyState
import com.kovhan.core.ui.component.quote.ReadOnlyQuoteCard
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.quote.component.WidgetQuoteTopBar
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteIntent
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteState

@Composable
internal fun WidgetQuoteScreen(
    state: WidgetQuoteState,
    intent: WidgetQuoteIntent,
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
            WidgetQuoteTopBar(
                titleRes = if (state.isDaily) {
                    DsR.string.widget_quote_daily_title
                } else {
                    DsR.string.widget_quote_title
                },
                menuVisible = state.menuVisible,
                canShowMenu = state.quote != null && !state.isDaily,
                intent = intent,
                onBack = onBack,
            )

            val quote = state.quote
            if (quote == null) {
                DefaultEmptyState()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                ) {
                    ReadOnlyQuoteCard(quote = quote)
                }
            }
        }
    }
}
