package com.kovhan.feature.main.presentation.devtools.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.navigation.DialogKey
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.devtools.DevDialogSample

@Composable
internal fun DevDialogPicker(
    dialogs: List<DevDialogSample>,
    onDialogClick: (DialogKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.bgElevated)
            .border(1.dp, colors.border, shape),
    ) {
        dialogs.forEachIndexed { index, sample ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDialogClick(sample.key) }
                    .padding(vertical = 13.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = sample.label,
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.W500,
                    ),
                )
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colors.textTertiary),
                )
            }
            if (index != dialogs.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 14.dp)
                        .background(colors.borderSubtle),
                )
            }
        }
    }
}
