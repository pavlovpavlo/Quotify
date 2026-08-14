package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

/** Юридичні посилання під CTA. Відкриваються у вбудованому WebView, як в авторизації. */
@Composable
internal fun PaywallLegalText(
    onOpenLink: (title: String, url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    val terms = stringResource(R.string.paywall_legal_terms)
    val privacy = stringResource(R.string.paywall_legal_privacy)
    val subscription = stringResource(R.string.paywall_legal_subscription)

    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = colors.textSecondary,
            fontWeight = FontWeight.W500,
            textDecoration = TextDecoration.Underline,
        ),
    )

    val annotated = buildAnnotatedString {
        fun link(label: String, url: String) {
            val annotation = LinkAnnotation.Clickable(
                tag = url,
                styles = linkStyles,
                linkInteractionListener = { onOpenLink(label, url) },
            )
            withLink(annotation) { append(label) }
        }

        link(terms, AppLinks.TERMS_OF_SERVICE)
        append(", ")
        link(privacy, AppLinks.PRIVACY_POLICY)
        append(", ")
        link(subscription, AppLinks.SUBSCRIPTION_POLICY)
    }

    Text(
        modifier = modifier,
        text = annotated,
        style = QuotifyMaterialTheme.typography.small.copy(
            fontSize = 12.sp,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
        ),
    )
}
