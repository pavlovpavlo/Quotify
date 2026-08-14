package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.feature.subscription.presentation.paywall.component.plancard.PaywallPlanCard
import androidx.compose.ui.platform.LocalConfiguration

@Composable
internal fun PaywallOffersBody(
    offer: PremiumOffer,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp),
    ) {
        PaywallHero()

        PaywallPlanCard(
            offer = offer,
            locale = locale,
            modifier = Modifier
                .offset(y = (-16).dp)
                .padding(bottom = 4.dp),
        )
    }
}
