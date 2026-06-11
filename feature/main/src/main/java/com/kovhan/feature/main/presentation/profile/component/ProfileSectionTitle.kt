package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun ProfileSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text,
        color = QuotifyMaterialTheme.colors.textSecondary,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.W600,
        ),
    )
}
