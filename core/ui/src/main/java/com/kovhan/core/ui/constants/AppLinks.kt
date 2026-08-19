package com.kovhan.core.ui.constants

/**
 * Single source of truth for outbound URLs the app links to. Anything that
 * opens the in-app [com.kovhan.core.navigation.WebViewKey] should pull
 * its URL from here so we don't sprinkle string literals across the codebase.
 */
object AppLinks {
    private const val SITE = "https://lumaday-site.pavlovpavlo2013.workers.dev"

    const val PRIVACY_POLICY = "$SITE/privacy-policy"
    const val TERMS_OF_SERVICE = "$SITE/public-offer"
    const val SUBSCRIPTION_POLICY = "$SITE/subscription-policy"
}
