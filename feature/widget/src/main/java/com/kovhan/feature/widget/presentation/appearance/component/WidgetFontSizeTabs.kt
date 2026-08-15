package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.widget.WidgetFontSize
import com.kovhan.core.ui.component.tabs.QuotifySegmentTab
import com.kovhan.core.ui.component.tabs.QuotifySegmentedTabs
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

private val SAMPLE_BASE_SIZE = 15.sp

@Composable
internal fun WidgetFontSizeTabs(
    selected: WidgetFontSize,
    onSelect: (WidgetFontSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val sample = stringResource(DsR.string.widget_appearance_font_sample)

    val items = WidgetFontSize.entries.map { QuotifySegmentTab(value = it, label = sample) }

    QuotifySegmentedTabs(
        modifier = modifier,
        items = items,
        selected = selected,
        onSelect = onSelect,
    ) { item, isSelected ->
        Text(
            text = item.label,
            color = if (isSelected) colors.textPrimary else colors.textTertiary,
            fontFamily = NewsreaderFamily,
            fontSize = SAMPLE_BASE_SIZE * item.value.scale,
        )
    }
}
