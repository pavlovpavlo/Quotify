package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.JetBrainsMonoFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState

@Composable
internal fun WidgetOptionsSection(
    state: WidgetSettingsState,
    intent: WidgetSettingsIntent,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusXl)

    Column(modifier = modifier.fillMaxWidth()) {
        WidgetSectionLabel(stringResource(DsR.string.widget_display_label))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.border, shape),
        ) {
            WidgetOptionRow(
                iconRes = DsR.drawable.ic_sun,
                iconTint = colors.accentPremium,
                iconContainer = colors.accentPremiumSoft,
                title = stringResource(DsR.string.widget_daily_quote_title),
                subtitle = stringResource(DsR.string.widget_daily_quote_subtitle),
            ) {
                QuotifySwitch(
                    checked = state.includeDailyQuote,
                    onCheckedChange = intent::onDailyQuoteToggled,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.size1)
                    .background(colors.borderSubtle),
            )

            WidgetOptionRow(
                iconRes = DsR.drawable.ic_clock,
                iconTint = colors.accentAi,
                iconContainer = colors.accentAiSoft,
                title = stringResource(DsR.string.widget_update_title),
                subtitle = stringResource(DsR.string.widget_update_subtitle),
                onClick = intent::onFrequencyClicked,
            ) {
                FrequencyPill(hours = state.frequencyHours)
            }
        }
    }
}

@Composable
private fun WidgetOptionRow(
    iconRes: Int,
    iconTint: Color,
    iconContainer: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val rowModifier = Modifier
        .fillMaxWidth()
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        .padding(horizontal = 14.dp, vertical = 13.dp)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size38)
                .clip(RoundedCornerShape(dimensions.radiusLg))
                .background(iconContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size19),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 10.dp),
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
            Text(
                text = subtitle,
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                ),
            )
        }

        trailing()
    }
}

@Composable
private fun FrequencyPill(hours: Int) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val label = if (hours <= 1) {
        stringResource(DsR.string.widget_frequency_hourly)
    } else {
        pluralStringResource(DsR.plurals.widget_frequency_hours, hours, hours)
    }

    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensions.radiusMd))
            .background(colors.bgSecondary)
            .border(dimensions.size1, colors.border, RoundedCornerShape(dimensions.radiusMd))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        text = label,
        color = colors.textSecondary,
        maxLines = 1,
        style = TextStyle(
            fontFamily = JetBrainsMonoFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.W500,
        ),
    )
}
