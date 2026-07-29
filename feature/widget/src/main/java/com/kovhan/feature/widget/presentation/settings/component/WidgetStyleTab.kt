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
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

private const val ANIM_MS = 160

@Composable
internal fun WidgetStyleTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
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
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(dimensions.radiusXl))
                .background(colors.bgElevated)
                .border(2.dp, borderColor, RoundedCornerShape(dimensions.radiusXl))
                .padding(7.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensions.radiusLg))
                    .background(colors.bgSecondary),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(dimensions.size20),
                    painter = painterResource(DsR.drawable.ic_image),
                    contentDescription = stringResource(DsR.string.widget_style_image_cd),
                    colorFilter = ColorFilter.tint(colors.textTertiary),
                )
            }
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
