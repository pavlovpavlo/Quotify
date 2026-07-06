package com.kovhan.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun AuthTitleBlock(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = typography.h2.copy(fontSize = 28.sp),
            color = colors.textPrimary,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = subtitle,
            style = typography.body.copy(fontSize = 14.sp),
            color = colors.textSecondary,
        )
    }
}
