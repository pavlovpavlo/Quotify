package com.kovhan.design.systems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.util.getDimensionMultiplier

val LocalDimensions = compositionLocalOf { defaultDimensions }

@Composable
internal fun provideDimensions(): QuotifyDimensions {
    val m = getDimensionMultiplier()
    val d = defaultDimensions
    return d.copy(
        // Spacing
        space1  = d.space1  * m,
        space2  = d.space2  * m,
        space3  = d.space3  * m,
        space4  = d.space4  * m,
        space5  = d.space5  * m,
        space6  = d.space6  * m,
        space8  = d.space8  * m,
        space10 = d.space10 * m,
        space12 = d.space12 * m,
        space16 = d.space16 * m,
        space20 = d.space20 * m,
        space24 = d.space24 * m,

        // Icons
        iconXs  = d.iconXs  * m,
        iconSm  = d.iconSm  * m,
        iconMd  = d.iconMd  * m,
        iconLg  = d.iconLg  * m,
        iconXl  = d.iconXl  * m,
        iconXxl = d.iconXxl * m,

        // Semantic component dims
        screenPadding       = d.screenPadding       * m,
        screenPaddingDouble = d.screenPaddingDouble * m,
        buttonHeight        = d.buttonHeight        * m,
        textFieldHeight     = d.textFieldHeight     * m,
        topBarHeight        = d.topBarHeight        * m,
        bottomBarHeight     = d.bottomBarHeight     * m,
        bottomBarIconSize   = d.bottomBarIconSize   * m,
        loaderSize          = d.loaderSize          * m,
        snackbarBottomPadding = d.snackbarBottomPadding * m,
        loginIllustrationHeight = d.loginIllustrationHeight * m,
    )
}

/**
 * Folio design system spacing & radii. 4px base scale.
 *
 *   space1=4, space2=8, space3=12, space4=16, space5=20, space6=24,
 *   space8=32, space10=40, space12=48, space16=64, space20=80, space24=96
 *
 *   radiusXs=2, radiusSm=4, radiusMd=6, radiusLg=10, radiusXl=14,
 *   radius2xl=20, radiusFull=999
 */
@Immutable
data class QuotifyDimensions(
    // Folio 4px-base spacing scale
    val space0: Dp  = 0.dp,
    val space1: Dp  = 4.dp,
    val space2: Dp  = 8.dp,
    val space3: Dp  = 12.dp,
    val space4: Dp  = 16.dp,
    val space5: Dp  = 20.dp,
    val space6: Dp  = 24.dp,
    val space8: Dp  = 32.dp,
    val space10: Dp = 40.dp,
    val space12: Dp = 48.dp,
    val space16: Dp = 64.dp,
    val space20: Dp = 80.dp,
    val space24: Dp = 96.dp,

    // Folio radii — slightly soft, bookish
    val radiusXs: Dp   = 2.dp,
    val radiusSm: Dp   = 4.dp,
    val radiusMd: Dp   = 6.dp,
    val radiusLg: Dp   = 10.dp,
    val radiusXl: Dp   = 14.dp,
    val radius2xl: Dp  = 20.dp,
    val radiusFull: Dp = 999.dp,

    // Icons
    val iconXs: Dp  = 12.dp,
    val iconSm: Dp  = 16.dp,
    val iconMd: Dp  = 20.dp,
    val iconLg: Dp  = 24.dp,
    val iconXl: Dp  = 32.dp,
    val iconXxl: Dp = 40.dp,

    // Semantic component dimensions
    val screenPadding: Dp        = 16.dp,
    val screenPaddingDouble: Dp  = 32.dp,
    val buttonHeight: Dp         = 48.dp,
    val textFieldHeight: Dp      = 56.dp,
    val topBarHeight: Dp         = 56.dp,
    val bottomBarHeight: Dp      = 62.dp,
    val bottomBarIconSize: Dp    = 24.dp,
    val loaderSize: Dp           = 52.dp,
    val snackbarBottomPadding: Dp = 36.dp,

    // Feature-specific
    val loginIllustrationHeight: Dp = 260.dp,
)

val defaultDimensions = QuotifyDimensions()
