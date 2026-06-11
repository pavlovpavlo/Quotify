package com.kovhan.feature.main.presentation.edit_profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditField
import com.kovhan.feature.main.presentation.profile.component.PrimaryActionButton

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FieldBottomSheet(
    field: EditField,
    initialValue: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val titleRes = when (field) {
        EditField.NAME -> R.string.edit_field_name
        EditField.USERNAME -> R.string.edit_field_username
        EditField.EMAIL -> R.string.edit_field_email
    }
    val initial = initialValue
    // Start with the cursor at the end of the prefilled value.
    var value by remember {
        mutableStateOf(TextFieldValue(initial, TextRange(initial.length)))
    }

    val canSave = value.text.trim().isNotEmpty() && value.text.trim() != initial.trim()

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(titleRes),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 22.dp)
                .imePadding(),
        ) {
            SheetFieldLabel(text = stringResource(titleRes))
            EditTextField(
                value = value,
                onValueChange = { value = it },
                prefix = if (field == EditField.USERNAME) "@" else null,
                keyboardType = if (field == EditField.EMAIL) KeyboardType.Email else KeyboardType.Text,
                autofocus = true,
            )
            Spacer(Modifier.height(18.dp))
            PrimaryActionButton(
                text = stringResource(R.string.edit_field_save),
                enabled = canSave,
                onClick = { onSave(value.text.trim()) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SheetFieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = QuotifyMaterialTheme.colors.textTertiary,
        modifier = Modifier.padding(bottom = 7.dp),
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.W600,
            letterSpacing = 0.05.em,
        ),
    )
}
