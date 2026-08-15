package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.models.widget.WidgetFeedback
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun WidgetFeedbackSection(
    onFeedback: (WidgetFeedback) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.space4),
    ) {
        Text(
            text = stringResource(DsR.string.widget_feedback_question),
            style = typography.bodyStrong,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(dimensions.space5)) {
            FeedbackButton(
                iconRes = DsR.drawable.ic_thumb_down,
                contentDescription = stringResource(DsR.string.widget_feedback_dislike_cd),
                tint = colors.accentPrimary,
                onClick = { onFeedback(WidgetFeedback.DISLIKE) },
            )
            FeedbackButton(
                iconRes = DsR.drawable.ic_thumb_up,
                contentDescription = stringResource(DsR.string.widget_feedback_like_cd),
                tint = colors.accentSaved,
                onClick = { onFeedback(WidgetFeedback.LIKE) },
            )
        }
    }
}

@Composable
private fun FeedbackButton(
    iconRes: Int,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = Modifier
            .size(dimensions.size44)
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .background(colors.bgSecondary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(dimensions.size22),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(tint),
        )
    }
}
