package com.kovhan.feature.addquote.presentation.details.component.tageditor

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.addquote.presentation.details.mvi.AiState

@Composable
internal fun AiTagsSection(
    aiState: AiState,
    tags: List<String>,
    locked: Boolean,
    isSelected: (String) -> Boolean,
    onToggle: (String) -> Unit,
    onGenerate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (aiState) {
        AiState.IDLE -> AiGenerateButton(
            loading = false,
            locked = locked,
            onClick = onGenerate,
            modifier = modifier,
        )

        AiState.LOADING -> AiGenerateButton(loading = true, locked = false, onClick = {}, modifier = modifier)
        AiState.DONE -> AiResults(
            tags = tags,
            isSelected = isSelected,
            onToggle = onToggle,
            modifier = modifier,
        )
    }
}

@Composable
private fun AiGenerateButton(
    loading: Boolean,
    locked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.size12)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.size52)
            .clip(shape)
            .background(colors.accentAiSoft)
            .then(if (loading) Modifier else Modifier.clickable(onClick = onClick)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensions.iconMd),
                    color = colors.accentAi,
                    strokeWidth = 2.dp,
                )
                Text(
                    text = stringResource(R.string.details_ai_loading),
                    style = typography.bodyStrong,
                    color = colors.accentAi,
                )
            } else {
                Image(
                    modifier = Modifier.size(dimensions.iconMd),
                    painter = painterResource(R.drawable.ic_sparkles),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colors.accentAi),
                )
                Text(
                    text = stringResource(R.string.details_ai_generate),
                    style = typography.bodyStrong,
                    color = colors.accentAi,
                )
            }
        }

        if (locked && !loading) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = dimensions.space4)
                    .size(dimensions.iconMd),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = stringResource(R.string.ai_tags_locked_cd),
                colorFilter = ColorFilter.tint(colors.accentAi),
            )
        }
    }
}

@Composable
private fun AiResults(
    tags: List<String>,
    isSelected: (String) -> Boolean,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(bottom = dimensions.space3, start = dimensions.size2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.size6),
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(R.drawable.ic_sparkles),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentAi),
            )
            Text(
                text = stringResource(R.string.details_ai_eyebrow),
                style = typography.eyebrow,
                color = colors.accentAi,
            )
        }

        if (tags.isEmpty()) {
            Text(
                modifier = Modifier.padding(start = dimensions.size2),
                text = stringResource(R.string.details_tag_empty),
                style = typography.caption,
                color = colors.textTertiary,
            )
        } else {
            TagCloud(
                tags = tags,
                accent = TagOptionAccent.Ai,
                isSelected = isSelected,
                onToggle = onToggle,
            )
        }
    }
}
