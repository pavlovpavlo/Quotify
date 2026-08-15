package com.kovhan.feature.widget.presentation.appearance.mvi

import com.kovhan.core.models.widget.WidgetFontSize
import com.kovhan.core.models.widget.WidgetTextAlign

interface WidgetAppearanceIntent {
    fun onToneSelected(toneId: String)
    fun onCoverSelected(coverId: String)
    fun onBlurToggled(enabled: Boolean)
    fun onBorderToggled(enabled: Boolean)
    fun onBorderToneSelected(toneId: String)
    fun onFontSizeSelected(fontSize: WidgetFontSize)
    fun onTextColorLight()
    fun onTextColorDark()
    fun onCustomTextColorRequested()
    fun onCustomTextColorPicked(argb: Int)
    fun onCustomTextColorDismissed()
    fun onTextAlignSelected(align: WidgetTextAlign)

    companion object {
        val Empty: WidgetAppearanceIntent = object : WidgetAppearanceIntent {
            override fun onToneSelected(toneId: String) = Unit
            override fun onCoverSelected(coverId: String) = Unit
            override fun onBlurToggled(enabled: Boolean) = Unit
            override fun onBorderToggled(enabled: Boolean) = Unit
            override fun onBorderToneSelected(toneId: String) = Unit
            override fun onFontSizeSelected(fontSize: WidgetFontSize) = Unit
            override fun onTextColorLight() = Unit
            override fun onTextColorDark() = Unit
            override fun onCustomTextColorRequested() = Unit
            override fun onCustomTextColorPicked(argb: Int) = Unit
            override fun onCustomTextColorDismissed() = Unit
            override fun onTextAlignSelected(align: WidgetTextAlign) = Unit
        }
    }
}
