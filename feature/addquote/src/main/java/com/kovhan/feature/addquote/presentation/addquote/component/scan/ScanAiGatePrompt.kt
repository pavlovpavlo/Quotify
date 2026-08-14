package com.kovhan.feature.addquote.presentation.addquote.component.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.feature.addquote.presentation.common.toGateContent

/**
 * Shown inside the dark camera frame when the user can't run the AI OCR — either
 * an upsell to subscribe (anonymous / free quota spent) or a "limit reached"
 * notice for subscribers. Mirrors the camera-permission prompt's look.
 */
@Composable
internal fun ScanAiGatePrompt(
    reason: AiDenialReason,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val content = reason.toGateContent()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.space6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.space4),
    ) {
        Image(
            modifier = Modifier.size(dimensions.iconXxl),
            painter = painterResource(R.drawable.ic_sparkles),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.7f)),
        )
        Text(
            text = stringResource(content.titleRes),
            style = typography.bodyStrong,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(content.messageRes),
            style = typography.caption,
            color = Color.White.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
        )
        if (content.isUpsell) {
            QuotifyButton(
                text = stringResource(R.string.ai_gate_upsell_cta),
                onClick = onUpgrade,
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Premium,
                size = QuotifyButtonSize.Medium,
            )
        }
    }
}
