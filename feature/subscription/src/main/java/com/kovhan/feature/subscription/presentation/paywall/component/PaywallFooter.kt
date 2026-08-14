package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.PremiumGradientButton
import com.kovhan.feature.subscription.presentation.common.displayPrice
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PaywallFooter(
    offers: List<PremiumOffer>,
    selectedOffer: PremiumOffer,
    isBusy: Boolean,
    onPlanSelected: (basePlanId: String) -> Unit,
    onSubscribe: () -> Unit,
    onOpenLink: (title: String, url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val locale = LocalConfiguration.current.locales[0]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.bgElevated),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.border),
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            offers.forEach { planOffer ->
                PaywallPlanRow(
                    offer = planOffer,
                    selected = planOffer.basePlanId == selectedOffer.basePlanId,
                    onClick = { onPlanSelected(planOffer.basePlanId) },
                )
            }

            VerticalSpacer(dimensions.space1)

            PremiumGradientButton(
                text = ctaText(selectedOffer),
                onClick = onSubscribe,
                loading = isBusy,
            )

            val trialDays = selectedOffer.freeTrialDays
            if (trialDays != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = trialNoteText(trialDays, selectedOffer.displayPrice(locale)),
                    textAlign = TextAlign.Center,
                    color = colors.textTertiary,
                    style = QuotifyMaterialTheme.typography.small.copy(fontSize = 11.5.sp),
                )
            }

            PaywallLegalText(
                onOpenLink = onOpenLink,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ctaText(offer: PremiumOffer): String {
    val trialDays = offer.freeTrialDays
    return if (trialDays != null) {
        pluralStringResource(DsR.plurals.paywall_cta_trial, trialDays, trialDays)
    } else {
        stringResource(DsR.string.paywall_cta_subscribe)
    }
}

@Composable
private fun trialNoteText(trialDays: Int, price: String): AnnotatedString {
    val highlight = QuotifyMaterialTheme.colors.textSecondary
    val raw = pluralStringResource(DsR.plurals.paywall_note_trial, trialDays, trialDays, price)

    val priceStart = if (price.isBlank()) -1 else raw.indexOf(price)

    return buildAnnotatedString {
        if (priceStart < 0) {
            append(raw)
            return@buildAnnotatedString
        }
        append(raw.substring(0, priceStart))
        withStyle(SpanStyle(fontWeight = FontWeight.W600, color = highlight)) {
            append(price)
        }
        append(raw.substring(priceStart + price.length))
    }
}
