package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.ui.component.tabs.QuotifySegmentTab
import com.kovhan.core.ui.component.tabs.QuotifySegmentedTabs
import com.kovhan.core.ui.mapper.WidgetBackgroundMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

internal enum class WidgetTextColorOption { LIGHT, DARK, CUSTOM }

@Composable
internal fun WidgetTextColorTabs(
    settings: WidgetStyleSettings,
    onLight: () -> Unit,
    onDark: () -> Unit,
    onCustom: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val textColor = settings.textColor
    val selected = when (textColor) {
        WidgetTextColor.Light -> WidgetTextColorOption.LIGHT
        WidgetTextColor.Dark -> WidgetTextColorOption.DARK
        is WidgetTextColor.Custom -> WidgetTextColorOption.CUSTOM
        WidgetTextColor.Auto -> {
            val darkTheme = QuotifyMaterialTheme.system.isDarkTheme
            if (WidgetBackgroundMapper.textColor(settings, darkTheme) == colorFor(WidgetTextColor.Light)) {
                WidgetTextColorOption.LIGHT
            } else {
                WidgetTextColorOption.DARK
            }
        }
    }

    val items = listOf(
        QuotifySegmentTab(
            value = WidgetTextColorOption.LIGHT,
            label = stringResource(DsR.string.widget_appearance_text_color_light),
        ),
        QuotifySegmentTab(
            value = WidgetTextColorOption.DARK,
            label = stringResource(DsR.string.widget_appearance_text_color_dark),
        ),
        QuotifySegmentTab(
            value = WidgetTextColorOption.CUSTOM,
            label = stringResource(DsR.string.widget_appearance_text_color_custom),
        ),
    )

    QuotifySegmentedTabs(
        modifier = modifier,
        items = items,
        selected = selected,
        onSelect = { option ->
            when (option) {
                WidgetTextColorOption.LIGHT -> onLight()
                WidgetTextColorOption.DARK -> onDark()
                WidgetTextColorOption.CUSTOM -> onCustom()
            }
        },
    ) { item, isSelected ->
        val dotShape = RoundedCornerShape(dimensions.radiusFull)
        val dotColor = when (item.value) {
            WidgetTextColorOption.LIGHT -> colorFor(WidgetTextColor.Light)
            WidgetTextColorOption.DARK -> colorFor(WidgetTextColor.Dark)
            WidgetTextColorOption.CUSTOM ->
                (textColor as? WidgetTextColor.Custom)?.let { Color(it.argb) }
                    ?: colors.accentPrimary
        }

        Box(
            modifier = Modifier
                .size(dimensions.size15)
                .clip(dotShape)
                .background(dotColor)
                .border(dimensions.size1, colors.borderStrong, dotShape),
        )
        Text(
            text = item.label,
            style = typography.caption.copy(
                fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W500,
            ),
            color = if (isSelected) colors.textPrimary else colors.textTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun colorFor(textColor: WidgetTextColor): Color =
    WidgetBackgroundMapper.textColor(textColor, Color.Transparent)
