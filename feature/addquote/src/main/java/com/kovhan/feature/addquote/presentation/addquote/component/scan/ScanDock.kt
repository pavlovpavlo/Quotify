package com.kovhan.feature.addquote.presentation.addquote.component.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.extensions.debouncedClickable
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.addquote.presentation.addquote.mvi.ScanMode

@Composable
internal fun ScanDock(
    mode: ScanMode,
    canProceed: Boolean,
    onGallery: () -> Unit,
    onShutter: () -> Unit,
    onRetake: () -> Unit,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.space6,
                vertical = dimensions.space5,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (mode) {
            ScanMode.LIVE, ScanMode.SCANNING -> {
                val enabled = mode == ScanMode.LIVE
                GalleryButton(onClick = onGallery, enabled = enabled)
                ShutterButton(onClick = onShutter, enabled = enabled)
                Spacer(modifier = Modifier.width(dimensions.size72))
            }

            ScanMode.SELECT -> {
                QuotifyButton(
                    text = stringResource(R.string.add_quote_scan_retake),
                    onClick = onRetake,
                    variant = QuotifyButtonVariant.Outlined,
                    accent = QuotifyButtonAccent.Neutral,
                    leadingIcon = painterResource(R.drawable.ic_camera),
                    sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.buttonHeight),
                )
                Spacer(modifier = Modifier.width(dimensions.space3))
                QuotifyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.add_quote_next),
                    onClick = onProceed,
                    enabled = canProceed,
                    variant = QuotifyButtonVariant.Filled,
                    accent = QuotifyButtonAccent.Primary,
                    sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.buttonHeight),
                )
            }
        }
    }
}

@Composable
private fun GalleryButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .width(dimensions.size72)
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.space1),
    ) {
        Image(
            modifier = Modifier.size(dimensions.size26),
            painter = painterResource(R.drawable.ic_image),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.textSecondary),
        )
        Text(
            text = stringResource(R.string.add_quote_scan_gallery),
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ShutterButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .size(dimensions.size70)
            .scale(if (pressed) 0.93f else 1f)
            .clip(CircleShape)
            .background(colors.accentPrimary)
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size56)
                .clip(CircleShape)
                .border(dimensions.size3, colors.textOnAccent, CircleShape),
        )
    }
}
