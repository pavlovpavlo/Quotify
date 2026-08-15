package com.kovhan.feature.widget.presentation.promo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.kovhan.core.ui.component.dialog.QuotifyDialogCloseButton
import com.kovhan.core.ui.component.widget.WidgetPreviewQuote
import com.kovhan.core.ui.component.widget.WidgetShowcasePreview
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val PREVIEW_SCALE = 0.64f

@Composable
fun WidgetPromoDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val roundedSpec = QuotifyButtonDefaults.sizeSpec(QuotifyButtonSize.Medium)
        .copy(shape = RoundedCornerShape(4.dp))

    QuotifyDialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                WidgetShowcasePreview(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                        .size(dimensions.size128),
                    scale = PREVIEW_SCALE,
                    quote = WidgetPreviewQuote(
                        text = stringResource(R.string.widget_appearance_preview_quote),
                        author = stringResource(R.string.widget_appearance_preview_author),
                        book = stringResource(R.string.widget_appearance_preview_book),
                    ),
                )

                Spacer(Modifier.height(dimensions.size20))

                Text(
                    text = stringResource(R.string.widget_promo_title),
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp,
                    ),
                )

                Spacer(Modifier.height(dimensions.size9))

                Text(
                    text = stringResource(R.string.widget_promo_text),
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                    ),
                )

                Spacer(Modifier.height(dimensions.size20))

                QuotifyButton(
                    text = stringResource(R.string.widget_promo_confirm),
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Filled,
                    accent = QuotifyButtonAccent.Primary,
                    size = QuotifyButtonSize.Medium,
                    sizeSpec = roundedSpec,
                )

                Spacer(Modifier.height(dimensions.size9))

                QuotifyButton(
                    text = stringResource(R.string.widget_promo_later),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = QuotifyButtonSize.Medium,
                    withRipple = false,
                    sizeSpec = roundedSpec,
                )
            }

            QuotifyDialogCloseButton(
                onClick = onDismiss,
                icon = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.widget_promo_close_cd),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensions.size12),
            )
        }
    }
}
