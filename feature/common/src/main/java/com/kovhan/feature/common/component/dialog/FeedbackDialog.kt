package com.kovhan.feature.common.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.dialog.QuotifyDialog
import com.kovhan.core.ui.component.dialog.QuotifyDialogCloseButton
import com.kovhan.core.ui.component.quote.QuoteTextInputField
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private enum class FeedbackChoice { LIKE, DISLIKE }

@Composable
fun FeedbackDialog(
    onSubmit: (liked: Boolean, comment: String) -> Unit,
    onDismiss: () -> Unit,
    initialLiked: Boolean? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val roundedSpec = QuotifyButtonDefaults.sizeSpec(QuotifyButtonSize.Medium)
        .copy(shape = RoundedCornerShape(4.dp))

    var choice by rememberSaveable {
        mutableStateOf(
            when (initialLiked) {
                true -> FeedbackChoice.LIKE
                false -> FeedbackChoice.DISLIKE
                null -> null
            },
        )
    }
    var comment by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var sent by rememberSaveable { mutableStateOf(false) }

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
                if (sent) {
                    Image(
                        modifier = Modifier.width(dimensions.size128),
                        painter = painterResource(R.drawable.img_feedback_thanks),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )

                    Spacer(Modifier.height(dimensions.size4))

                    DialogTitle(
                        text = stringResource(R.string.feedback_done_title),
                        modifier = Modifier.padding(horizontal = dimensions.size24),
                    )

                    Spacer(Modifier.height(dimensions.size9))

                    DialogBody(stringResource(R.string.feedback_done_text))

                    Spacer(Modifier.height(dimensions.size20))

                    QuotifyButton(
                        text = stringResource(R.string.feedback_done_confirm),
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        variant = QuotifyButtonVariant.Filled,
                        accent = QuotifyButtonAccent.Primary,
                        size = QuotifyButtonSize.Medium,
                        sizeSpec = roundedSpec,
                    )
                } else {
                    DialogTitle(
                        text = stringResource(R.string.feedback_title),
                        modifier = Modifier.padding(horizontal = dimensions.size24),
                    )

                    Spacer(Modifier.height(dimensions.size9))

                    DialogBody(stringResource(R.string.feedback_text))

                    Spacer(Modifier.height(dimensions.size20))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.size12),
                    ) {
                        ChoiceCard(
                            iconRes = R.drawable.ic_thumb_down,
                            label = stringResource(R.string.feedback_dislike),
                            selected = choice == FeedbackChoice.DISLIKE,
                            accent = colors.accentPrimary,
                            accentSoft = colors.accentPrimarySoft,
                            onClick = { choice = FeedbackChoice.DISLIKE },
                        )
                        ChoiceCard(
                            iconRes = R.drawable.ic_thumb_up,
                            label = stringResource(R.string.feedback_like),
                            selected = choice == FeedbackChoice.LIKE,
                            accent = colors.accentSaved,
                            accentSoft = colors.accentSavedSoft,
                            onClick = { choice = FeedbackChoice.LIKE },
                        )
                    }

                    AnimatedVisibility(
                        visible = choice != null,
                        enter = fadeIn() + expandVertically(),
                    ) {
                        val liked = choice == FeedbackChoice.LIKE

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(Modifier.height(dimensions.size16))

                            QuoteTextInputField(
                                value = comment,
                                onValueChange = { comment = it },
                                label = stringResource(
                                    if (liked) {
                                        R.string.feedback_comment_label_optional
                                    } else {
                                        R.string.feedback_comment_label
                                    },
                                ),
                                placeholder = stringResource(
                                    if (liked) {
                                        R.string.feedback_comment_hint_positive
                                    } else {
                                        R.string.feedback_comment_hint_negative
                                    },
                                ),
                                minHeight = dimensions.size96,
                            )

                            Spacer(Modifier.height(dimensions.size12))

                            QuotifyButton(
                                text = stringResource(R.string.feedback_send),
                                onClick = {
                                    sent = true
                                    onSubmit(liked, comment.text.trim())
                                },
                                modifier = Modifier.fillMaxWidth(),
                                variant = QuotifyButtonVariant.Filled,
                                accent = QuotifyButtonAccent.Primary,
                                size = QuotifyButtonSize.Medium,
                                sizeSpec = roundedSpec,
                            )
                        }
                    }
                }
            }

            QuotifyDialogCloseButton(
                onClick = onDismiss,
                icon = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.feedback_close_cd),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensions.size12),
            )
        }
    }
}

@Composable
private fun DialogTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = QuotifyMaterialTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = NewsreaderFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 24.sp,
        ),
    )
}

@Composable
private fun DialogBody(text: String) {
    Text(
        text = text,
        color = QuotifyMaterialTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
        ),
    )
}

@Composable
private fun RowScope.ChoiceCard(
    iconRes: Int,
    label: String,
    selected: Boolean,
    accent: Color,
    accentSoft: Color,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusLg)

    val content = if (selected) accent else colors.textSecondary
    val border = if (selected) accent else colors.borderStrong
    val background = if (selected) accentSoft else colors.bgElevated

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(shape)
            .background(background)
            .border(dimensions.size1, border, shape)
            .clickable(onClick = onClick)
            .padding(vertical = dimensions.size18, horizontal = dimensions.size10),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.size9),
    ) {
        Image(
            modifier = Modifier.size(dimensions.size30),
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(content),
        )
        Text(
            text = label,
            color = content,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}
