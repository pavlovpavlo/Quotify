package com.kovhan.feature.main.presentation.faq.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val CHEVRON_COLLAPSED_DEGREES = 0f
private const val CHEVRON_EXPANDED_DEGREES = 90f

@Composable
internal fun FaqAccordionItem(
    question: String,
    answer: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) CHEVRON_EXPANDED_DEGREES else CHEVRON_COLLAPSED_DEGREES,
        label = "faq_chevron",
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 14.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = question,
                color = colors.textPrimary,
                style = typography.body.copy(fontWeight = FontWeight.W600),
            )
            Image(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
                    .rotate(chevronRotation),
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textTertiary),
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 42.dp, bottom = 14.dp),
                text = answer,
                color = colors.textSecondary,
                style = typography.readingBody,
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 14.dp)
                    .background(colors.borderSubtle),
            )
        }
    }
}
