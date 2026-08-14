package com.kovhan.feature.subscription.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private const val BOLD_OPEN = "<b>"
private const val BOLD_CLOSE = "</b>"

@Composable
internal fun rememberBoldMarkup(
    text: String,
    boldWeight: FontWeight = FontWeight.W700,
    boldColor: Color = Color.Unspecified,
): AnnotatedString = remember(text, boldWeight, boldColor) {
    buildAnnotatedString {
        var index = 0
        while (index < text.length) {
            val open = text.indexOf(BOLD_OPEN, index)
            if (open < 0) {
                append(text.substring(index))
                break
            }
            val close = text.indexOf(BOLD_CLOSE, open + BOLD_OPEN.length)
            if (close < 0) {
                append(text.substring(index))
                break
            }
            append(text.substring(index, open))
            withStyle(SpanStyle(fontWeight = boldWeight, color = boldColor)) {
                append(text.substring(open + BOLD_OPEN.length, close))
            }
            index = close + BOLD_CLOSE.length
        }
    }
}
