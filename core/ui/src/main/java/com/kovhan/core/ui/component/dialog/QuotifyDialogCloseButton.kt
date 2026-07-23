package com.kovhan.core.ui.component.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.kovhan.core.ui.component.button.QuotifyButtonColors
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme


@Composable
fun QuotifyDialogCloseButton(
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyIconButton(
        onClick = onClick,
        modifier = modifier,
        size = dimensions.size30,
        colors = QuotifyButtonColors(
            container = colors.bgSecondary,
            content = colors.textSecondary,
            border = Color.Transparent,
            disabledContainer = colors.bgSecondary,
            disabledContent = colors.textSecondary,
            disabledBorder = Color.Transparent,
        ),
    ) {
        Icon(
            modifier = Modifier.size(dimensions.size16),
            painter = icon,
            contentDescription = contentDescription,
            tint = LocalContentColor.current,
        )
    }
}
