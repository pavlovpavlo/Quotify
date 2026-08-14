package com.kovhan.core.ui.component.premium

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.extensions.noRippleClickable
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

/**
 * Розмиває платний вміст. `Modifier.blur` реально малює лише з API 31,
 * тож нижче падаємо на прозорість — вміст усе одно має читатись як недоступний.
 */
fun Modifier.premiumBlur(radius: androidx.compose.ui.unit.Dp = 12.dp): Modifier =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) blur(radius) else alpha(0.4f)

/**
 * Замок поверх розмитого вмісту: перехоплює всі дотики й веде на екран підписки.
 * Кладеться в той самий [Box], що й вміст, одразу після нього.
 */
@Composable
fun BoxScope.PremiumLockOverlay(
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = Modifier
            .matchParentSize()
            .background(colors.bgElevated.copy(alpha = 0.35f))
            .noRippleClickable { onUnlock() },
    )

    Column(
        modifier = modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size56)
                .background(colors.accentPremiumSoft, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.iconLg),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = stringResource(R.string.premium_locked_cd),
                colorFilter = ColorFilter.tint(colors.accentPremium),
            )
        }

        QuotifyButton(
            modifier = Modifier.padding(top = dimensions.space4),
            text = stringResource(R.string.premium_unlock_cta),
            onClick = onUnlock,
            accent = QuotifyButtonAccent.Premium,
            size = QuotifyButtonSize.Medium,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = 44.dp),
        )
    }
}
