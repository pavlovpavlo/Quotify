package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

private const val ANIM_MS = 160

/**
 * One selectable quote source. Tapping the radio always selects it; tapping the
 * rest of the row runs [onRowClick] — which opens the editor for playlists and
 * simply selects for the built-in sources.
 */
@Composable
internal fun WidgetSourceCard(
    title: String,
    count: Int,
    iconRes: Int,
    selected: Boolean,
    showChevron: Boolean,
    onRowClick: () -> Unit,
    onRadioClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusLg)

    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.accentPrimary else colors.border,
        animationSpec = tween(ANIM_MS),
        label = "source-border",
    )
    val background by animateColorAsState(
        targetValue = if (selected) colors.accentPrimarySoft else colors.bgElevated,
        animationSpec = tween(ANIM_MS),
        label = "source-bg",
    )

    Row(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(1.5.dp, borderColor, shape)
            .clickable(onClick = onRowClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SourceRadio(selected = selected, onClick = onRadioClick)

        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(dimensions.size40)
                .clip(RoundedCornerShape(dimensions.radiusLg))
                .background(if (selected) colors.accentPrimary else colors.bgSecondary),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size20),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (selected) colors.textOnAccent else colors.textSecondary,
                ),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
            Text(
                text = pluralStringResource(DsR.plurals.widget_quote_count, count, count),
                color = colors.textTertiary,
                style = TextStyle(fontFamily = InterFamily, fontSize = 12.5.sp),
            )
        }

        if (showChevron) {
            Image(
                modifier = Modifier.size(dimensions.size18),
                painter = painterResource(DsR.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textTertiary),
            )
        }
    }
}

@Composable
private fun SourceRadio(
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dotScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(ANIM_MS),
        label = "radio-dot",
    )

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (selected) colors.accentPrimary else colors.borderStrong,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .scale(dotScale)
                .clip(CircleShape)
                .background(colors.accentPrimary),
        )
    }
}
