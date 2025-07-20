package com.kovhan.core.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.kovhan.core.ui.component.spacer.HorizontalSpacer
import com.kovhan.core.ui.extensions.debouncedClickable
import com.kovhan.design.systems.QuotifyMaterialTheme

class QuotifyBtn {
}
@Preview
@Composable
fun QuotifyBtnPreview() {
    QuotifyBtn(
        modifier = Modifier,
        text = "Button",
        enabled = true,
        onClick = {}
    )
}

@Composable
fun QuotifyBtn(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    QuotifyBtn(
        modifier = modifier,
        text = text,
        background = QuotifyMaterialTheme.colors.btnColor,
        textColor = QuotifyMaterialTheme.colors.textColorGray,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun QuotifyBtn(
    modifier: Modifier = Modifier,
    text: String,
    background: Color,
    textColor: Color,
    enabled: Boolean = true,
    startIcon: Painter? = null,
    isApplyDisabledBackground: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(QuotifyMaterialTheme.dimensions.buttonHeight)
            .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.corner_radius_6))
            .background(brush = SolidColor(background))
            .debouncedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.wrapContentSize()
                .padding(horizontal = QuotifyMaterialTheme.dimensions.size_10),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (startIcon != null) {
                Image(
                    modifier = Modifier.size(QuotifyMaterialTheme.dimensions.size_20),
                    painter = startIcon,
                    contentDescription = null
                )

                HorizontalSpacer(QuotifyMaterialTheme.dimensions.space_10)
            }

            Text(
                modifier = Modifier.wrapContentSize(),
                text = text,
                color = textColor,
                style = QuotifyMaterialTheme.typography.subtitleSemibold
            )
        }

        if (!enabled && isApplyDisabledBackground) {
            Box(
                modifier = Modifier.matchParentSize()
                    .background(color = QuotifyMaterialTheme.colors.btnColorDisabled)
            )
        }
    }
}