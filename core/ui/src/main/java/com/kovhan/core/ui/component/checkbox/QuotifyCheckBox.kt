package com.kovhan.core.ui.component.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kovhan.core.ui.component.spacer.HorizontalSpacer
import com.kovhan.design.systems.QuotifyMaterialTheme

@Preview
@Composable
fun QuotifyCheckBoxPreview(
) {
    QuotifyCheckBox(
        text = "Я ознайомлений з правилами. ",
    ) {
        // no-op
    }
}

@Composable
fun QuotifyCheckBox(
    modifier: Modifier = Modifier,
    text: String = "",
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .toggleable(
                value = isChecked,
                role = Role.Checkbox,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.Top
    ) {
//        Image(
//            modifier = Modifier
//                .padding(top = QuotifyMaterialTheme.dimensions.space1)
//                .size(size = QuotifyMaterialTheme.dimensions.iconSm),
//            painter = painterResource(
//                id = if (isChecked) {
//                    QuotifyMaterialTheme.images.iconCheckboxChecked
//                } else {
//                    QuotifyMaterialTheme.images.iconCheckboxUnChecked
//                }
//            ),
//            contentScale = ContentScale.FillBounds,
//            contentDescription = null,
//        )

        HorizontalSpacer(space = QuotifyMaterialTheme.dimensions.space3)

        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
                .padding(top = QuotifyMaterialTheme.dimensions.space1),
            text = text,
            style = QuotifyMaterialTheme.typography.small,
            color = QuotifyMaterialTheme.colors.textPrimary,
            textAlign = TextAlign.Start,
        )
    }
}