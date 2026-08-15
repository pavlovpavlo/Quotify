package com.kovhan.feature.widget.presentation.appearance

import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetFontSize
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextAlign
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetStyleSettingsUseCase
import com.kovhan.feature.widget.glance.WidgetRefresher
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceIntent
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetAppearanceViewModel @Inject constructor(
    private val observeWidgetSettings: ObserveWidgetSettingsUseCase,
    private val setStyleSettings: SetWidgetStyleSettingsUseCase,
    private val widgetRefresher: WidgetRefresher,
) : BaseViewModel<WidgetAppearanceState, UiEffect>(WidgetAppearanceState()),
    WidgetAppearanceIntent {

    private var style: WidgetStyle? = null

    fun initialize(style: WidgetStyle) {
        if (this.style == style) return
        this.style = style
        viewModelScope.launch {
            val settings = observeWidgetSettings().first().appearance[style]
            publishState { copy(isLoading = false, settings = settings) }
        }
    }

    override fun onToneSelected(toneId: String) = updateClassic { copy(toneId = toneId) }

    override fun onCoverSelected(coverId: String) = updateCover { copy(coverId = coverId) }

    override fun onBlurToggled(enabled: Boolean) = updateCover { copy(blurEnabled = enabled) }

    override fun onBorderToggled(enabled: Boolean) = update { settings ->
        when (settings) {
            is WidgetStyleSettings.Minimal -> settings.copy(borderEnabled = enabled)
            is WidgetStyleSettings.Classic -> settings.copy(borderEnabled = enabled)
            is WidgetStyleSettings.Cover -> settings.copy(borderEnabled = enabled)
        }
    }

    override fun onBorderToneSelected(toneId: String) = update { settings ->
        val border = WidgetBorderColor.Tone(toneId)
        when (settings) {
            is WidgetStyleSettings.Minimal -> settings.copy(borderColor = border)
            is WidgetStyleSettings.Classic -> settings.copy(borderColor = border)
            is WidgetStyleSettings.Cover -> settings.copy(borderColor = border)
        }
    }

    override fun onFontSizeSelected(fontSize: WidgetFontSize) = update { settings ->
        when (settings) {
            is WidgetStyleSettings.Minimal -> settings.copy(fontSize = fontSize)
            is WidgetStyleSettings.Classic -> settings.copy(fontSize = fontSize)
            is WidgetStyleSettings.Cover -> settings.copy(fontSize = fontSize)
        }
    }

    override fun onTextColorLight() = setTextColor(WidgetTextColor.Light)

    override fun onTextColorDark() = setTextColor(WidgetTextColor.Dark)

    override fun onCustomTextColorRequested() =
        publishState { copy(pickingCustomTextColor = true) }

    override fun onCustomTextColorPicked(argb: Int) {
        publishState { copy(pickingCustomTextColor = false) }
        setTextColor(WidgetTextColor.Custom(argb))
    }

    override fun onCustomTextColorDismissed() =
        publishState { copy(pickingCustomTextColor = false) }

    override fun onTextAlignSelected(align: WidgetTextAlign) = update { settings ->
        when (settings) {
            is WidgetStyleSettings.Minimal -> settings.copy(textAlign = align)
            is WidgetStyleSettings.Classic -> settings.copy(textAlign = align)
            is WidgetStyleSettings.Cover -> settings.copy(textAlign = align)
        }
    }

    private fun setTextColor(textColor: WidgetTextColor) = update { settings ->
        when (settings) {
            is WidgetStyleSettings.Minimal -> settings.copy(textColor = textColor)
            is WidgetStyleSettings.Classic -> settings.copy(textColor = textColor)
            is WidgetStyleSettings.Cover -> settings.copy(textColor = textColor)
        }
    }

    private fun updateClassic(
        transform: WidgetStyleSettings.Classic.() -> WidgetStyleSettings.Classic,
    ) = update { settings ->
        (settings as? WidgetStyleSettings.Classic)?.transform() ?: settings
    }

    private fun updateCover(
        transform: WidgetStyleSettings.Cover.() -> WidgetStyleSettings.Cover,
    ) = update { settings ->
        (settings as? WidgetStyleSettings.Cover)?.transform() ?: settings
    }

    private fun update(transform: (WidgetStyleSettings) -> WidgetStyleSettings) {
        val updated = transform(uiState.value.settings)
        if (updated == uiState.value.settings) return
        publishState { copy(settings = updated) }
        viewModelScope.launch {
            setStyleSettings(updated)
            widgetRefresher.refresh()
        }
    }
}
