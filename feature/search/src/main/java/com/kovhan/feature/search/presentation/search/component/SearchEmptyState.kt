package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SearchEmptyState(
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.size24, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(DsR.string.search_empty_title),
            style = typography.h4,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(dimensions.size6))

        Text(
            modifier = Modifier.widthIn(max = 240.dp),
            text = stringResource(DsR.string.search_empty_sub),
            style = typography.body,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
        )
    }
}
