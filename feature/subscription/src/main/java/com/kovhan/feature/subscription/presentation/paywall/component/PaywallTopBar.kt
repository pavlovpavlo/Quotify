package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.button.QuotifyTextBtn
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PaywallTopBar(
    isRestoring: Boolean,
    onRestore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        QuotifyTextBtn(
            text = stringResource(DsR.string.paywall_restore),
            onClick = onRestore,
            enabled = !isRestoring,
            loading = isRestoring,
        )

        QuotifyIconButton(onClick = onClose, size = 36.dp) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(DsR.drawable.ic_close),
                contentDescription = stringResource(DsR.string.paywall_close_cd),
                colorFilter = ColorFilter.tint(colors.textSecondary),
            )
        }
    }
}
