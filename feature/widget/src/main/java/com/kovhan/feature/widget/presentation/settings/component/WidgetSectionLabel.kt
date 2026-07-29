package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

/** Uppercase eyebrow label above a widget-settings section (design `.q-aq-label`). */
@Composable
internal fun WidgetSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp, start = 2.dp),
        text = text.uppercase(),
        color = QuotifyMaterialTheme.colors.textTertiary,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.12.em,
        ),
    )
}
