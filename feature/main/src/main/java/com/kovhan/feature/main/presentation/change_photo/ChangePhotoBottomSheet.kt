package com.kovhan.feature.main.presentation.change_photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChangePhotoBottomSheet(
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onRemovePhoto: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(R.string.edit_photo_sheet_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            PhotoOptionRow(
                iconRes = R.drawable.ic_camera,
                label = stringResource(R.string.edit_photo_take),
                onClick = onTakePhoto,
            )
            PhotoOptionRow(
                iconRes = R.drawable.ic_image,
                label = stringResource(R.string.edit_photo_gallery),
                onClick = onPickGallery,
            )
            PhotoOptionRow(
                iconRes = R.drawable.ic_trash,
                label = stringResource(R.string.edit_photo_remove),
                onClick = onRemovePhoto,
                danger = true,
            )
        }
    }
}

@Composable
private fun PhotoOptionRow(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    danger: Boolean = false,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd)
    val tint = if (danger) colors.accentPrimary else colors.textSecondary
    val labelColor = if (danger) colors.accentPrimary else colors.textPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.bgSecondary)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint),
        )
        Text(
            text = label,
            color = labelColor,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
            ),
        )
    }
}
