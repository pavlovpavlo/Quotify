package com.kovhan.feature.main.presentation.edit_profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun ConfirmDialog(
    iconRes: Int,
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusXl)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
                .padding(horizontal = 24.dp)
                .shadow(16.dp, shape)
                .clip(shape)
                .background(colors.bgElevated)
                .border(1.dp, colors.border, shape)
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colors.accentPrimarySoft),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(23.dp),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colors.accentPrimary),
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                ),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                ),
            )

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                DialogButton(
                    text = cancelText,
                    danger = false,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                DialogButton(
                    text = confirmText,
                    danger = true,
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    danger: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd)
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(shape)
            .then(
                if (danger) {
                    Modifier.background(colors.accentPrimary)
                } else {
                    Modifier.border(1.dp, colors.borderStrong, shape)
                },
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (danger) colors.textOnAccent else colors.textPrimary,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}
