package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Small pill chip with colored background, used as decorative tags floating
 * around the hero illustration on the second onboarding slide.
 */
@Composable
internal fun OnboardingChip(
    text: String,
    background: Color,
    contentColor: Color,
    border: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .border(width = 1.dp, color = border, shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        text = text,
        style = QuotifyMaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.05.sp,
        ),
        color = contentColor,
    )
}
