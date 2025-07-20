package com.kovhan.core.ui.component.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .wrapContentWidth()
            .height(space)
    )
}

@Composable
fun HorizontalSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .wrapContentHeight()
            .width(space)
    )
}
