package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.ui.component.widget.WidgetPreviewCard
import com.kovhan.core.ui.component.widget.WidgetPreviewDefaults
import com.kovhan.core.ui.component.widget.WidgetPreviewQuote
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

private const val ANIM_MS = 160

@Composable
internal fun WidgetStyleTab(
    label: String,
    settings: WidgetStyleSettings,
    previewQuote: WidgetPreviewQuote,
    selected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.accentPrimary else colors.border,
        animationSpec = tween(ANIM_MS),
        label = "style-border",
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) colors.accentPrimaryHover else colors.textSecondary,
        animationSpec = tween(ANIM_MS),
        label = "style-label",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(dimensions.radiusXl))
                .border(dimensions.size2, borderColor, RoundedCornerShape(dimensions.radiusXl))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )
                .padding(dimensions.size7),
        ) {
            WidgetPreviewCard(
                modifier = Modifier.fillMaxSize(),
                settings = settings,
                quote = previewQuote,
                scale = WidgetPreviewDefaults.ThumbnailScale,
                cornerRadius = dimensions.radiusLg,
            )

            EditBadge(
                modifier = Modifier.align(Alignment.TopEnd),
                selected = selected,
                onClick = onEdit,
            )
        }

        Text(
            text = label,
            color = labelColor,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}

@Composable
private fun EditBadge(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusFull)
    val tint = if (selected) colors.accentPrimary else colors.textSecondary

    Box(
        modifier = modifier
            .size(dimensions.size26)
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, if (selected) colors.accentPrimary else colors.borderStrong, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(14.dp),
            painter = painterResource(DsR.drawable.ic_pencil),
            contentDescription = stringResource(DsR.string.widget_style_edit_cd),
            colorFilter = ColorFilter.tint(tint),
        )
    }
}
