package com.kovhan.feature.addquote.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyTextBtn
import com.kovhan.core.ui.component.dialog.QuotifyDialog
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.domain.ai.AiDenialReason

@Composable
internal fun AiLimitDialog(
    reason: AiDenialReason,
    onDismiss: () -> Unit,
    onReviewSubscriptions: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val content = reason.toGateContent()

    QuotifyDialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .padding(top = dimensions.space6)
                .size(dimensions.size56)
                .clip(CircleShape)
                .background(colors.accentAiSoft),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.iconLg),
                painter = painterResource(R.drawable.ic_sparkles),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentAi),
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.space4, start = dimensions.space6, end = dimensions.space6),
            text = stringResource(content.titleRes),
            style = typography.h4,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.space2, start = dimensions.space6, end = dimensions.space6),
            text = stringResource(content.messageRes),
            style = typography.body,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        QuotifyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensions.space6,
                    start = dimensions.space6,
                    end = dimensions.space6,
                    bottom = dimensions.space6,
                ),
            text = stringResource(
                if (content.isUpsell) R.string.ai_gate_upsell_cta else R.string.ai_gate_review_cta,
            ),
            onClick = onReviewSubscriptions,
            accent = QuotifyButtonAccent.Premium,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
        )

        Spacer(modifier = Modifier.size(dimensions.space1))

        QuotifyTextBtn(
            text = stringResource(R.string.ai_gate_not_now),
            onClick = onDismiss,
            modifier = Modifier.padding(bottom = dimensions.space5),
            accent = QuotifyButtonAccent.Neutral,
        )
    }
}
