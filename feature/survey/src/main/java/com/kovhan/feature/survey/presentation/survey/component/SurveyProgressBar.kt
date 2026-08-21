package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kovhan.design.systems.QuotifyMaterialTheme

private const val SEGMENT_ANIMATION_MS = 220

@Composable
internal fun SurveyProgressBar(
    stepCount: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.size20),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size5),
    ) {
        repeat(stepCount) { index ->
            val color by animateColorAsState(
                targetValue = if (index <= currentIndex) colors.accentPrimary else colors.border,
                animationSpec = tween(SEGMENT_ANIMATION_MS),
                label = "survey_progress_segment",
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(dimensions.size4)
                    .clip(RoundedCornerShape(dimensions.radiusFull))
                    .background(color),
            )
        }
    }
}
