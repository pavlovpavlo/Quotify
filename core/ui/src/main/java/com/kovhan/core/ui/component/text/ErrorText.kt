package com.kovhan.core.ui.component.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
fun ErrorText(modifier: Modifier,
              @StringRes error: Int,
              paddingTop: Dp = QuotifyMaterialTheme.dimensions.space1,
              paddingEnd: Dp = QuotifyMaterialTheme.dimensions.space3,
              paddingStart: Dp = QuotifyMaterialTheme.dimensions.space3,
              textStyle: TextStyle = QuotifyMaterialTheme.typography.caption,
              textColor: Color = QuotifyMaterialTheme.colors.error
){
    Text(
        modifier = modifier
            .padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
            )
            .fillMaxWidth(),
        text = stringResource(id = error),
        color = textColor,
        style = textStyle,
    )
}