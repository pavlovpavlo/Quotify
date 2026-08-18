package com.kovhan.feature.entitydetails.presentation.quote_edit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.toolbar.QuotifyTopAppBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun QuoteEditTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyTopAppBar(
        modifier = modifier,
        title = stringResource(DsR.string.collection_details_quote_edit_title),
        navigationIcon = {
            QuotifyIconButton(
                onClick = onBack,
                size = dimensions.iconXxl,
                debounceInterval = 0L,
            ) {
                Image(
                    modifier = Modifier.size(dimensions.size22),
                    painter = painterResource(DsR.drawable.ic_back),
                    contentDescription = stringResource(DsR.string.details_back_cd),
                    colorFilter = ColorFilter.tint(colors.textPrimary),
                )
            }
        },
    )
}

@Composable
internal fun QuoteEditFooter(
    onSave: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.space5,
                end = dimensions.space5,
                top = dimensions.space3,
                bottom = dimensions.space5,
            ),
    ) {
        QuotifyButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(DsR.string.details_save),
            onClick = onSave,
            enabled = enabled,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
        )
    }
}
