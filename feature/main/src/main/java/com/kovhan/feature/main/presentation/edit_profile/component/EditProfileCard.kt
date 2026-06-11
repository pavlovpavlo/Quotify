package com.kovhan.feature.main.presentation.edit_profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun EditCard(content: @Composable () -> Unit) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(colors.bgElevated)
            .border(1.dp, colors.border, shape),
    ) {
        content()
    }
}

@Composable
internal fun EditSectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 8.dp),
        color = QuotifyMaterialTheme.colors.textSecondary,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.W600,
        ),
    )
}

@Composable
internal fun EditRow(
    iconRes: Int,
    label: String,
    value: String,
    showDivider: Boolean,
    onClick: () -> Unit,
    danger: Boolean = false,
) {
    val colors = QuotifyMaterialTheme.colors
    val iconTint = if (danger) colors.accentPrimary else colors.textSecondary
    val labelColor = if (danger) colors.accentPrimary else colors.textPrimary

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
            )
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                color = labelColor,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                ),
            )
            if (value.isNotBlank()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = value,
                    color = colors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.sp,
                    ),
                )
            }
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textTertiary),
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 14.dp, end = 14.dp)
                    .background(colors.borderSubtle),
            )
        }
    }
}
