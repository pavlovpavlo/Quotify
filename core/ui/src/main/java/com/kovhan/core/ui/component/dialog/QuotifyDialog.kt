package com.kovhan.core.ui.component.dialog

import android.graphics.Color as AndroidColor
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import com.kovhan.core.ui.locale.ProvideAppLocale
import com.kovhan.design.systems.QuotifyMaterialTheme


@Composable
fun QuotifyDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusXl)

    val isDarkTheme = QuotifyMaterialTheme.system.isDarkTheme

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false
        ),
    ) {
        val view = LocalView.current
        LaunchedEffect(view, isDarkTheme) {
            var parent = view.parent
            while (parent != null && parent !is DialogWindowProvider) {
                parent = (parent as? View)?.parent
            }
            val dialogWindow = (parent as? DialogWindowProvider)?.window ?: return@LaunchedEffect
            dialogWindow.setDimAmount(0f)
            dialogWindow.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
            )
            dialogWindow.statusBarColor = AndroidColor.TRANSPARENT
            dialogWindow.navigationBarColor = AndroidColor.TRANSPARENT
            WindowCompat.setDecorFitsSystemWindows(dialogWindow, false)
            val insetsController = WindowCompat.getInsetsController(dialogWindow, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
        ProvideAppLocale {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.bgOverlay)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismissRequest,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = modifier
                        .then(
                            if (maxWidth == null)
                                Modifier.fillMaxWidth()
                            else
                                Modifier.widthIn(max = maxWidth)
                        )
                        .padding(dimensions.size24)
                        .shadow(dimensions.size24, shape)
                        .clip(shape)
                        .background(colors.bgElevated)
                        .border(dimensions.size1, colors.border, shape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }
        }
    }
}
