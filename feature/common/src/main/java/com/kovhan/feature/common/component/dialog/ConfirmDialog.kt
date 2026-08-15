package com.kovhan.feature.common.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.dialog.QuotifyDialog
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.QuotifyMaterialTheme.dimensions
import com.kovhan.design.systems.R

@Composable
fun ConfirmDialog(
    iconRes: Int,
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors

    val roundedSpec = QuotifyButtonDefaults.sizeSpec(QuotifyButtonSize.Medium)
        .copy(shape = RoundedCornerShape(4.dp))

    QuotifyDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.size22,
                    end = dimensions.size22,
                    top = dimensions.size22,
                    bottom = dimensions.size18,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                QuotifyButton(
                    text = confirmText,
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Filled,
                    accent = QuotifyButtonAccent.Primary,
                    size = QuotifyButtonSize.Medium,
                    sizeSpec = roundedSpec,
                )

                Spacer(Modifier.height(dimensions.size9))

                QuotifyButton(
                    text = cancelText,
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Outlined,
                    accent = QuotifyButtonAccent.Neutral,
                    size = QuotifyButtonSize.Medium,
                    colors = QuotifyButtonDefaults.colors(
                        variant = QuotifyButtonVariant.Outlined,
                        accent = QuotifyButtonAccent.Neutral,
                    ).copy(border = colors.borderStrong),
                    sizeSpec = roundedSpec,
                )
            }
        }
    }
}