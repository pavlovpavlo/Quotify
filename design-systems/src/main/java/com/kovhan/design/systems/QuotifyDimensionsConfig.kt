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

    // Raw dp scale — literal size tokens (size1 = 1.dp, …) for one-off values
    val size1: Dp   = 1.dp,
    val size2: Dp   = 2.dp,
    val size3: Dp   = 3.dp,
    val size4: Dp   = 4.dp,
    val size5: Dp   = 5.dp,
    val size6: Dp   = 6.dp,
    val size7: Dp   = 7.dp,
    val size8: Dp   = 8.dp,
    val size8_6: Dp   = 8.6.dp,
    val size9: Dp   = 9.dp,
    val size10: Dp  = 10.dp,
    val size11: Dp  = 11.dp,
    val size12: Dp  = 12.dp,
    val size13: Dp  = 13.dp,
    val size14: Dp  = 14.dp,
    val size15: Dp  = 15.dp,
    val size16: Dp  = 16.dp,
    val size17: Dp  = 17.dp,
    val size18: Dp  = 18.dp,
    val size19: Dp  = 19.dp,
    val size20: Dp  = 20.dp,
    val size21: Dp  = 21.dp,
    val size22: Dp  = 22.dp,
    val size23: Dp  = 23.dp,
    val size24: Dp  = 24.dp,
    val size28: Dp  = 28.dp,
    val size30: Dp  = 30.dp,
    val size38: Dp  = 38.dp,
    val size40: Dp  = 40.dp,
    val size128: Dp = 128.dp,
    val size163: Dp = 163.dp,
)

val defaultDimensions = QuotifyDimensions()
