package com.kovhan.feature.survey.presentation.done

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.component.SurveyEyebrow
import com.kovhan.feature.survey.presentation.done.component.SurveyConfetti
import com.kovhan.feature.survey.presentation.done.component.SurveyDoneBadge
import com.kovhan.feature.survey.presentation.done.component.SurveyRewardCard

@Composable
internal fun SurveyDoneContent(
    reward: SurveyRewardState,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val depleted = reward is SurveyRewardState.Depleted

    Box(modifier = modifier.fillMaxSize()) {
        if (!depleted) SurveyConfetti()

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.size24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (depleted) {
                    Image(
                        modifier = Modifier.width(dimensions.size128),
                        painter = painterResource(DsR.drawable.img_feedback_thanks),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                } else {
                    SurveyDoneBadge()
                }

                Spacer(Modifier.height(dimensions.size16))

                SurveyEyebrow(
                    stringResource(
                        if (depleted) {
                            DsR.string.survey_done_empty_eyebrow
                        } else {
                            DsR.string.survey_done_eyebrow
                        },
                    ),
                )

                Spacer(Modifier.height(dimensions.size8))

                Text(
                    text = stringResource(
                        if (depleted) {
                            DsR.string.survey_done_empty_title
                        } else {
                            DsR.string.survey_done_title
                        },
                    ),
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 32.sp,
                    ),
                )

                Spacer(Modifier.height(dimensions.size8))

                Text(
                    text = stringResource(
                        if (depleted) {
                            DsR.string.survey_done_empty_body
                        } else {
                            DsR.string.survey_done_body
                        },
                    ),
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 22.sp,
                    ),
                )

                if (reward is SurveyRewardState.Granted) {
                    Spacer(Modifier.height(dimensions.size22))

                    SurveyRewardCard(
                        coverId = reward.coverId,
                        title = stringResource(DsR.string.survey_done_reward_title),
                        subtitle = stringResource(DsR.string.survey_done_reward_body),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensions.size20,
                        end = dimensions.size20,
                        top = dimensions.size10,
                        bottom = dimensions.size20,
                    ),
                verticalArrangement = Arrangement.spacedBy(dimensions.size4),
            ) {
                QuotifyButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        if (depleted) {
                            DsR.string.survey_done_secondary
                        } else {
                            DsR.string.survey_done_primary
                        },
                    ),
                    onClick = if (depleted) onSecondaryClick else onPrimaryClick,
                    variant = QuotifyButtonVariant.Filled,
                    accent = QuotifyButtonAccent.Primary,
                    sizeSpec = QuotifyButtonDefaults.pillSizeSpec(dimensions.size52),
                )

                if (!depleted) {
                    QuotifyButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(DsR.string.survey_done_secondary),
                        onClick = onSecondaryClick,
                        variant = QuotifyButtonVariant.Ghost,
                        accent = QuotifyButtonAccent.Neutral,
                        withRipple = false,
                        sizeSpec = QuotifyButtonDefaults.pillSizeSpec(dimensions.size44),
                    )
                }
            }
        }
    }
}
