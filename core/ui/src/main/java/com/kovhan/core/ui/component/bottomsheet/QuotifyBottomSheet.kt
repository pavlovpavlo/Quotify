package com.kovhan.core.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import android.view.WindowManager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import com.kovhan.core.ui.locale.ProvideAppLocale
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Base modal bottom sheet for the app. Renders a floating, rounded card with a
 * drag handle and an optional centered [title], then the [content] below it.
 *
 * System bar icon colors are flipped to match the current theme so they stay
 * readable over the sheet's scrim.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuotifyBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    height: BottomSheetHeight = BottomSheetHeight.WRAP_CONTENT,
    containerColor: Color = QuotifyMaterialTheme.colors.bgElevated,
    floatingStyle: Boolean = false,
    cornerRadius: Dp = QuotifyMaterialTheme.dimensions.radius2xl,
    floatingBottomSpacing: Dp = QuotifyMaterialTheme.dimensions.space6,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(Unit) {
        sheetState.show()
    }

    val shouldUseFloating = floatingStyle && height != BottomSheetHeight.FULL_SCREEN

    val horizontalPadding =
        if (shouldUseFloating) QuotifyMaterialTheme.dimensions.space6
        else QuotifyMaterialTheme.dimensions.space0

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
            .padding(horizontal = horizontalPadding)
            .statusBarsPadding(),
        containerColor = Color.Transparent,
        scrimColor = QuotifyMaterialTheme.colors.bgOverlay,
        dragHandle = null,
    ) {
        val view = LocalView.current
        val isDarkTheme = QuotifyMaterialTheme.system.isDarkTheme

        LaunchedEffect(view, isDarkTheme) {
            val dialogWindow = (view.parent as? DialogWindowProvider)?.window
            if (dialogWindow != null) {
                dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                WindowCompat.setDecorFitsSystemWindows(dialogWindow, false)
                val insetsController = WindowCompat.getInsetsController(dialogWindow, view)
                insetsController.isAppearanceLightStatusBars = !isDarkTheme
                insetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }
        }

        val contentModifier = when (height) {
            BottomSheetHeight.WRAP_CONTENT -> Modifier.fillMaxWidth()
            BottomSheetHeight.HALF_SCREEN -> Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)

            BottomSheetHeight.FULL_SCREEN -> Modifier.fillMaxSize()
        }

        val shape = if (shouldUseFloating) {
            RoundedCornerShape(cornerRadius)
        } else {
            RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
        }

        val cardModifier = Modifier
            .then(
                if (shouldUseFloating) Modifier.padding(bottom = floatingBottomSpacing)
                else Modifier,
            )
            .clip(shape)
            .background(containerColor)

        ProvideAppLocale {
            Box(modifier = Modifier.wrapContentHeight()) {
                Column(modifier = contentModifier.then(cardModifier)) {
                    BottomSheetHeader(title = title)
                    content()
                }
            }
        }
    }
}
