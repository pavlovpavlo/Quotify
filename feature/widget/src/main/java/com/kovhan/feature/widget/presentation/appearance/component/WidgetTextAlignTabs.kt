package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.widget.WidgetTextAlign
import com.kovhan.core.ui.component.tabs.QuotifySegmentTab
import com.kovhan.core.ui.component.tabs.QuotifySegmentedTabs
import com.kovhan.design.systems.R as DsR

@Composable
internal fun WidgetTextAlignTabs(
    selected: WidgetTextAlign,
    onSelect: (WidgetTextAlign) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotifySegmentedTabs(
        modifier = modifier,
        items = listOf(
            QuotifySegmentTab(
                value = WidgetTextAlign.START,
                label = stringResource(DsR.string.widget_appearance_align_start),
            ),
            QuotifySegmentTab(
                value = WidgetTextAlign.CENTER,
                label = stringResource(DsR.string.widget_appearance_align_center),
            ),
        ),
        selected = selected,
        onSelect = onSelect,
    )
}
