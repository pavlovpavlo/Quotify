package com.kovhan.feature.widget.presentation.settings

import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSourcesUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetDailyQuoteUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetFrequencyUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetSourceUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetStyleUseCase
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsEffect
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetSettingsViewModel @Inject constructor(
    observeWidgetSettings: ObserveWidgetSettingsUseCase,
    observeWidgetSources: ObserveWidgetSourcesUseCase,
    private val setSource: SetWidgetSourceUseCase,
    private val setDailyQuote: SetWidgetDailyQuoteUseCase,
    private val setFrequency: SetWidgetFrequencyUseCase,
    private val setStyle: SetWidgetStyleUseCase,
) : BaseViewModel<WidgetSettingsState, WidgetSettingsEffect>(WidgetSettingsState()),
    WidgetSettingsIntent {

    init {
        combine(observeWidgetSettings(), observeWidgetSources()) { settings, sources ->
            settings to sources
        }.onEach { (settings, sources) ->
            publishState {
                copy(
                    isLoading = false,
                    selectedSource = settings.source,
                    includeDailyQuote = settings.includeDailyQuote,
                    frequencyHours = settings.frequencyHours,
                    style = settings.style,
                    allCount = sources.allCount,
                    favouritesCount = sources.favouritesCount,
                    playlists = sources.playlists,
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onSourceSelected(source: WidgetSource) {
        publishState { copy(selectedSource = source) }
        viewModelScope.launch { setSource(source) }
    }

    override fun onCreatePlaylistClicked() = publishEffect(WidgetSettingsEffect.OpenCreatePlaylist)

    override fun onEditPlaylistClicked(playlistId: String) =
        publishEffect(WidgetSettingsEffect.OpenEditPlaylist(playlistId))

    override fun onDailyQuoteToggled(enabled: Boolean) {
        publishState { copy(includeDailyQuote = enabled) }
        viewModelScope.launch { setDailyQuote(enabled) }
    }

    override fun onFrequencyClicked() =
        publishEffect(WidgetSettingsEffect.OpenFrequencyPicker(uiState.value.frequencyHours))

    override fun onStyleSelected(style: WidgetStyle) {
        publishState { copy(style = style) }
        viewModelScope.launch { setStyle(style) }
    }

    override fun onAddToHomeClicked() = publishEffect(WidgetSettingsEffect.WidgetAdded)

    /** Called from the entry when the frequency sheet returns a value. */
    fun applyFrequency(hours: Int) {
        publishState { copy(frequencyHours = hours) }
        viewModelScope.launch { setFrequency(hours) }
    }
}
