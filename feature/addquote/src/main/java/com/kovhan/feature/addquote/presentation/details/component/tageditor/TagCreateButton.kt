package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun TagCreateButton(
    query: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensions.size12))
            .background(colors.accentPrimarySoft)
            .clickable(onClick = onClick)
            .padding(horizontal = dimensions.space4, vertical = dimensions.size11),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
    ) {
        Image(
            modifier = Modifier.size(dimensions.iconSm),
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.accentPrimary),
        )
        Text(
            text = stringResource(R.string.details_tag_create, query),
            style = typography.body.copy(fontWeight = FontWeight.W600),
            color = colors.accentPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
