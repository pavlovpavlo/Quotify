package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButtonColors
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DailyQuoteFavouriteButton(
    isFavourite: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val container = if (isFavourite) colors.accentPrimary else colors.bgElevated
    val content = if (isFavourite) colors.textOnAccent else colors.accentPremiumHover
    val border =
        if (isFavourite) colors.accentPrimary else colors.accentPremium.copy(alpha = 0.32f)

    QuotifyIconButton(
        onClick = onClick,
        modifier = modifier.shadow(dimensions.size1, CircleShape),
        loading = isLoading,
        debounceInterval = 300L,
        size = dimensions.size38,
        colors = QuotifyButtonColors(
            container = container,
            content = content,
            border = border,
            disabledContainer = container,
            disabledContent = content,
            disabledBorder = border,
        ),
    ) {
        Icon(
            modifier = Modifier.size(dimensions.size19),
            painter = painterResource(
                if (isFavourite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline,
            ),
            contentDescription = stringResource(R.string.daily_quote_save_cd),
            tint = LocalContentColor.current,
        )
    }
}
