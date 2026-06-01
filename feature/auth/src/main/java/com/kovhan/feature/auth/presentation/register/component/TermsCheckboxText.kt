package com.kovhan.feature.auth.presentation.register.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.extensions.noRippleClickable
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val TAG_PRIVACY = "privacy"
private const val TAG_TERMS = "terms"

@Composable
internal fun TermsCheckboxText(
    accepted: Boolean,
    onToggle: (Boolean) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val linkStyle = SpanStyle(
        color = colors.textSecondary,
        fontWeight = FontWeight.W500,
        textDecoration = TextDecoration.Underline,
    )

    val annotated: AnnotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = colors.textSecondary)) {
            append(stringResource(R.string.auth_agree_intro))
            append(' ')
        }
        pushStringAnnotation(tag = TAG_TERMS, annotation = TAG_TERMS)
        withStyle(linkStyle) {
            append(stringResource(R.string.auth_terms))
        }
        pop()
        withStyle(SpanStyle(color = colors.textSecondary)) {
            append(' ')
            append(stringResource(R.string.auth_and))
            append(' ')
        }
        pushStringAnnotation(tag = TAG_PRIVACY, annotation = TAG_PRIVACY)
        withStyle(linkStyle) {
            append(stringResource(R.string.auth_privacy))
        }
        pop()
        withStyle(SpanStyle(color = colors.textSecondary)) {
            append('.')
        }
    }

    Row(
        modifier = modifier.padding(top = 2.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Checkbox(
            checked = accepted,
            onToggle = { onToggle(!accepted) },
            modifier = Modifier.padding(top = 1.dp),
        )

        ClickableText(
            modifier = Modifier.padding(start = 9.dp),
            text = annotated,
            style = QuotifyMaterialTheme.typography.small.copy(
                fontSize = 12.sp,
                lineHeight = 1.45.em,
                color = colors.textSecondary,
            ),
            onClick = { offset ->
                val privacy = annotated.getStringAnnotations(TAG_PRIVACY, offset, offset).firstOrNull()
                val terms = annotated.getStringAnnotations(TAG_TERMS, offset, offset).firstOrNull()
                when {
                    privacy != null -> onPrivacyPolicyClick()
                    terms != null -> onTermsOfServiceClick()
                    else -> onToggle(!accepted)
                }
            },
        )
    }
}

@Composable
private fun Checkbox(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd)
    val checkProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 140),
        label = "checkboxProgress",
    )

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(shape)
            .background(if (checked) colors.accentPrimary else colors.bgElevated)
            .border(
                width = 1.5.dp,
                color = if (checked) colors.accentPrimary else colors.borderStrong,
                shape = shape,
            )
            .noRippleClickable(onToggle),
        contentAlignment = Alignment.Center,
    ) {
        if (checkProgress > 0f) {
            Canvas(modifier = Modifier.size(12.dp)) {
                val w = size.width
                val h = size.height
                val path = Path().apply {
                    moveTo(w * 0.18f, h * 0.52f)
                    lineTo(w * 0.42f, h * 0.76f)
                    lineTo(w * 0.84f, h * 0.28f)
                }
                drawPath(
                    path = path,
                    color = colors.textOnAccent.copy(alpha = checkProgress),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )
            }
        }
    }
}
