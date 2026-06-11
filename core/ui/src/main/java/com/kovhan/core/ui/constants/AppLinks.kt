package com.kovhan.core.ui.constants

/**
 * Single source of truth for outbound URLs the app links to. Anything that
 * opens the in-app [com.kovhan.core.navigation.WebViewKey] should pull
 * its URL from here so we don't sprinkle string literals across the codebase.
 *
 * Real links â€” privacy / terms / support â€” are still TBD; until then they
 * point at google.com as a working placeholder so the WebView screen and
 * navigation flow can be tested end-to-end.
 */
object AppLinks {
    const val PRIVACY_POLICY = "https://www.google.com"
    const val TERMS_OF_SERVICE = "https://www.google.com"
}
