package com.kovhan.design.systems.util

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

object ColorUtil {

    fun parseColor(color: String, defaultColor: Color): Color {
        return runCatching {
            Color(AndroidColor.parseColor("#$color"))
        }.getOrDefault(defaultColor)
    }

}