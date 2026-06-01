package com.kovhan.feature.auth.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val TAG_TERMS = "terms"
private const val TAG_PRIVACY = "privacy"

@Composable
internal fun LegalFooter(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val linkStyle = SpanStyle(
        color = colors.textSecondary,
        fontWeight = FontWeight.W500,
        textDecoration = TextDecoration.Underline,
    )

    val annotated: AnnotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = colors.textTertiary)) {
            append(stringResource(R.string.auth_legal_intro))
            append(' ')
        }
        pushStringAnnotation(tag = TAG_TERMS, annotation = TAG_TERMS)
        withStyle(linkStyle) {
            append(stringResource(R.string.auth_terms))
        }
        pop()
        withStyle(SpanStyle(color = colors.textTertiary)) {
            append(' ')
            append(stringResource(R.string.auth_and))
            append(' ')
        }
        pushStringAnnotation(tag = TAG_PRIVACY, annotation = TAG_PRIVACY)
        withStyle(linkStyle) {
            append(stringResource(R.string.auth_privacy))
        }
        pop()
        withStyle(SpanStyle(color = colors.textTertiary)) {
            append('.')
        }
    }

    ClickableText(
        modifier = modifier.fillMaxWidth(),
        text = annotated,
        style = QuotifyMaterialTheme.typography.small.copy(
            fontSize = 11.sp,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
        ),
        onClick = { offset ->
            annotated.getStringAnnotations(TAG_TERMS, offset, offset).firstOrNull()?.let {
                onTermsClick()
                return@ClickableText
            }
            annotated.getStringAnnotations(TAG_PRIVACY, offset, offset).firstOrNull()?.let {
                onPrivacyClick()
            }
        },
    )
}
