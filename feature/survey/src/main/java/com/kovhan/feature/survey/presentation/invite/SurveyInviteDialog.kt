package com.kovhan.feature.survey.presentation.invite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.survey.SurveyInvite
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.dialog.QuotifyDialog
import com.kovhan.core.ui.component.dialog.QuotifyDialogCloseButton
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.component.SurveyEyebrow
import com.kovhan.feature.survey.presentation.invite.component.SurveyRewardChip

@Composable
fun SurveyInviteDialog(
    invite: SurveyInvite?,
    onStart: () -> Unit,
    onLater: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val roundedSpec = QuotifyButtonDefaults.sizeSpec(QuotifyButtonSize.Medium)
        .copy(shape = RoundedCornerShape(4.dp))

    QuotifyDialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensions.size22,
                        end = dimensions.size22,
                        top = dimensions.size22,
                        bottom = dimensions.size18,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.width(dimensions.size128),
                    painter = painterResource(DsR.drawable.img_feedback_thanks),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )

                Spacer(Modifier.height(dimensions.size10))

                SurveyEyebrow(stringResource(DsR.string.survey_invite_eyebrow))

                Spacer(Modifier.height(dimensions.size8))

                Text(
                    text = invite?.title ?: stringResource(DsR.string.survey_invite_title),
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp,
                    ),
                )

                Spacer(Modifier.height(dimensions.size6))

                Text(
                    text = invite?.body ?: stringResource(DsR.string.survey_invite_body),
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                    ),
                )

                Spacer(Modifier.height(dimensions.size14))

                SurveyRewardChip(invite?.reward ?: stringResource(DsR.string.survey_invite_reward))

                Spacer(Modifier.height(dimensions.size18))

                QuotifyButton(
                    text = stringResource(DsR.string.survey_invite_start),
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Filled,
                    accent = QuotifyButtonAccent.Primary,
                    size = QuotifyButtonSize.Medium,
                    sizeSpec = roundedSpec,
                )

                Spacer(Modifier.height(dimensions.size9))

                QuotifyButton(
                    text = stringResource(DsR.string.survey_invite_later),
                    onClick = onLater,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Outlined,
                    accent = QuotifyButtonAccent.Neutral,
                    size = QuotifyButtonSize.Medium,
                    colors = QuotifyButtonDefaults.colors(
                        variant = QuotifyButtonVariant.Outlined,
                        accent = QuotifyButtonAccent.Neutral,
                    ).copy(border = colors.borderStrong),
                    sizeSpec = roundedSpec,
                )

                QuotifyButton(
                    text = stringResource(DsR.string.survey_invite_skip),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = QuotifyButtonSize.Medium,
                    withRipple = false,
                    sizeSpec = roundedSpec,
                )
            }

            QuotifyDialogCloseButton(
                onClick = onDismiss,
                icon = painterResource(DsR.drawable.ic_close),
                contentDescription = stringResource(DsR.string.survey_invite_close_cd),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensions.size12),
            )
        }
    }
}
