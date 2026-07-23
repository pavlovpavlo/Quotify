package com.kovhan.feature.addquote.presentation.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DetailsFooter(
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
            text = stringResource(R.string.details_save),
            onClick = onSave,
            enabled = enabled,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
        )
    }
}
