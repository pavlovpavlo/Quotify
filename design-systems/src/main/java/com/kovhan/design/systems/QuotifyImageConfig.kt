package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.annotation.DrawableRes
import com.kovhan.design.systems.R

val LocaleQuotifyImages = compositionLocalOf { quotifyLightImages }

@Immutable
class QuotifyImagePalette(
    val home: Int,
    val quotes: Int,
    val favorites: Int,
    val profile: Int,
    val loginBgIcon: Int = R.drawable.quotation_icon,
    val quoteDecoration: Int = R.drawable.quote_onboarding_icon,
    val onboardingSlide1: Int = R.drawable.ic_onboarding_1,
    val onboardingSlide2: Int = R.drawable.ic_onboarding_2,
    val onboardingSlide3Ukr: Int = R.drawable.ic_onboarding_3_ukr,
    val onboardingSlide3Eng: Int = R.drawable.ic_onboarding_3_eng,
    val welcomeIllustration: Int = R.drawable.complete_image,
    val googleLogo: Int = R.drawable.ic_google,
    val dockTabLibrary: Int = R.drawable.ic_tab_library,
    val dockTabProfile: Int = R.drawable.ic_tab_profile,
    val dockFabOpen: Int = R.drawable.ic_open_tab_menu,
    val dockFabClose: Int = R.drawable.ic_close_tab_menu,
    val dockInputKeyboard: Int = R.drawable.ic_keyboard_tab_menu,
    val dockInputScan: Int = R.drawable.ic_scan_tab_menu,
    val dockInputVoice: Int = R.drawable.ic_micro_tab_menu,
)

val quotifyLightImages = QuotifyImagePalette(
    home = R.drawable.ic_home,
    quotes = R.drawable.ic_quotes,
    favorites = R.drawable.ic_favorites,
    profile = R.drawable.ic_profile,
)

val quotifyDarkImages = QuotifyImagePalette(
    home = R.drawable.ic_home,
    quotes = R.drawable.ic_quotes,
    favorites = R.drawable.ic_favorites,
    profile = R.drawable.ic_profile,
)