package com.kovhan.feature.widget.presentation.picker.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PlaylistPickerFooter(
    enabled: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.bgPrimary),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.size1)
                .background(colors.borderSubtle),
        )
        Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp)) {
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(DsR.string.playlist_save),
                onClick = onSave,
                enabled = enabled,
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Primary,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(52.dp),
            )
        }
    }
}
