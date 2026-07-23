package com.kovhan.feature.addquote.presentation.addquote.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.ui.component.tabs.QuotifySegmentTab
import com.kovhan.core.ui.component.tabs.QuotifySegmentedTabs
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun AddQuoteTabs(
    selected: AddQuoteTab,
    onSelect: (AddQuoteTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val images = QuotifyMaterialTheme.images

    val items = listOf(
        QuotifySegmentTab(
            value = AddQuoteTab.TEXT,
            label = stringResource(R.string.add_quote_tab_text),
            icon = images.dockInputKeyboard,
        ),
        QuotifySegmentTab(
            value = AddQuoteTab.SCAN,
            label = stringResource(R.string.add_quote_tab_scan),
            icon = images.dockInputScan,
        ),
        QuotifySegmentTab(
            value = AddQuoteTab.VOICE,
            label = stringResource(R.string.add_quote_tab_voice),
            icon = images.dockInputVoice,
        ),
    )

    QuotifySegmentedTabs(
        modifier = modifier,
        items = items,
        selected = selected,
        onSelect = onSelect,
    )
}
