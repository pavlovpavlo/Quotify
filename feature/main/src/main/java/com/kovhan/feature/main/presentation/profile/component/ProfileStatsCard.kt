package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun ProfileStatsCard(
    quotes: Int,
    books: Int,
    folders: Int,
    authors: Int,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)

    Row(
        modifier = modifier
            .shadow(2.dp, shape)
            .clip(shape)
            .background(colors.bgElevated)
            .border(1.dp, colors.border, shape)
            .height(IntrinsicSize.Min)
            .padding(vertical = 16.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatColumn(value = quotes, label = stringResource(R.string.profile_stat_quotes))
        StatDivider()
        StatColumn(value = books, label = stringResource(R.string.profile_stat_books))
        StatDivider()
        StatColumn(value = folders, label = stringResource(R.string.profile_stat_folders))
        StatDivider()
        StatColumn(value = authors, label = stringResource(R.string.profile_stat_authors))
    }
}

@Composable
private fun RowScope.StatColumn(
    value: Int,
    label: String,
) {
    val colors = QuotifyMaterialTheme.colors
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = value.toString(),
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = NewsreaderFamily,
                fontSize = 23.sp,
                fontWeight = FontWeight.W600,
                fontFeatureSettings = "tnum",
            ),
        )
        Text(
            text = label,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.W700,
                letterSpacing = 0.1.em,
            ),
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(QuotifyMaterialTheme.colors.border),
    )
}
