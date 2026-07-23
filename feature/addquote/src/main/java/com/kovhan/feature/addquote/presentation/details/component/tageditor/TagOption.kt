package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

/** Which accent a [TagOption] carries — AI-suggested (blue) or recent (olive). */
enum class TagOptionAccent { Ai, Recent }

@Composable
internal fun TagOption(
    label: String,
    selected: Boolean,
    accent: TagOptionAccent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusFull)

    val container = when {
        selected && accent == TagOptionAccent.Ai -> colors.accentAiSoft
        selected -> colors.accentSavedSoft
        else -> colors.bgSecondary
    }
    val content = when {
        selected && accent == TagOptionAccent.Ai -> colors.accentAi
        selected -> colors.accentSaved
        accent == TagOptionAccent.Ai -> colors.accentAi
        else -> colors.textSecondary
    }
    val borderColor = if (selected) Color.Transparent else colors.border

    val leadingIcon = when {
        selected -> R.drawable.ic_check
        accent == TagOptionAccent.Ai -> R.drawable.ic_sparkles
        else -> null
    }

    Row(
        modifier = modifier
            .clip(shape)
            .background(container)
            .border(dimensions.size1, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = dimensions.space3, vertical = dimensions.size7),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size6),
    ) {
        if (leadingIcon != null) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(leadingIcon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(content),
            )
        }
        Text(
            text = "#$label",
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = content,
        )
    }
}
