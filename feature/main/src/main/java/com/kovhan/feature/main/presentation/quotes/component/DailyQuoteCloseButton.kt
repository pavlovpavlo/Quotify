package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButtonColors
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DailyQuoteCloseButton(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val premium = colors.accentPremium

    QuotifyIconButton(
        onClick = onClose,
        modifier = modifier,
        size = dimensions.size28,
        colors = QuotifyButtonColors(
            container = premium.copy(alpha = 0.14f),
            content = premium,
            border = Color.Transparent,
            disabledContainer = premium.copy(alpha = 0.14f),
            disabledContent = premium,
            disabledBorder = Color.Transparent,
        ),
    ) {
        Icon(
            modifier = Modifier.size(dimensions.size15),
            painter = painterResource(R.drawable.ic_close),
            contentDescription = stringResource(R.string.daily_quote_remove_cd),
            tint = LocalContentColor.current,
        )
    }
}
