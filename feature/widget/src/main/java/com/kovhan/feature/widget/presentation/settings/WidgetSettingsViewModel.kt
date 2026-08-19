package com.kovhan.feature.widget.presentation.settings

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.WidgetAdded
import com.kovhan.core.analytics.event.WidgetUpdated
import com.kovhan.feature.widget.presentation.analytics.toAnalytics
import com.kovhan.feature.widget.presentation.analytics.toSnapshot
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.core.models.widget.WidgetFeedback
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.navigation.WidgetExitAction
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.feedback.use_case.ObserveFeedbackGivenUseCase
import com.kovhan.domain.widget.use_case.settings.GetAppliedWidgetSourceUseCase
import com.kovhan.domain.widget.use_case.settings.IsWidgetSettingsAppliedUseCase
import com.kovhan.domain.widget.use_case.settings.MarkWidgetSettingsAppliedUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSourcesUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetDailyQuoteUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetFrequencyUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetSourceUseCase
import com.kovhan.domain.widget.use_case.settings.SetWidgetStyleUseCase
import com.kovhan.feature.widget.glance.WidgetRefresher
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsEffect
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetSettingsViewModel @Inject constructor(
    observeWidgetSettings: ObserveWidgetSettingsUseCase,
    observeWidgetSources: ObserveWidgetSourcesUseCase,
    observeFeedbackGiven: ObserveFeedbackGivenUseCase,
    private val setSource: SetWidgetSourceUseCase,
    private val setDailyQuote: SetWidgetDailyQuoteUseCase,
    private val setFrequency: SetWidgetFrequencyUseCase,
    private val setStyle: SetWidgetStyleUseCase,
    private val isWidgetSettingsApplied: IsWidgetSettingsAppliedUseCase,
    private val markWidgetSettingsApplied: MarkWidgetSettingsAppliedUseCase,
    private val analytics: AnalyticsTracker,
    private val getAppliedWidgetSource: GetAppliedWidgetSourceUseCase,
    private val widgetRefresher: WidgetRefresher,
) : BaseViewModel<WidgetSettingsState, WidgetSettingsEffect>(WidgetSettingsState()),
    WidgetSettingsIntent {

    private var currentSettings: WidgetSettings? = null
    private var pendingWrite: Job? = null

    init {
        combine(
            observeWidgetSettings(),
            observeWidgetSources(),
            observeFeedbackGiven(FeedbackSource.WIDGET),
        ) { settings, sources, feedbackGiven ->
            Triple(settings, sources, feedbackGiven)
        }.onEach { (settings, sources, feedbackGiven) ->
            currentSettings = settings
            val applied = isWidgetSettingsApplied(settings)
            publishState {
                copy(
                    isLoading = false,
                    feedbackGiven = feedbackGiven,
                    hasPendingChanges = !applied,
                    selectedSource = settings.source,
                    includeDailyQuote = settings.includeDailyQuote,
                    frequencyHours = settings.frequencyHours,
                    style = settings.style,
                    appearance = settings.appearance,
                    allCount = sources.allCount,
                    favouritesCount = sources.favouritesCount,
                    playlists = sources.playlists,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun persist(write: suspend () -> Unit) {
        pendingWrite = viewModelScope.launch { write() }
    }

    suspend fun applyToWidget(alreadyPlaced: Boolean) {
        pendingWrite?.join()
        val current = currentSettings ?: return
        val appliedSource = getAppliedWidgetSource()
        widgetRefresher.refresh(rotate = appliedSource != current.source)
        markWidgetSettingsApplied(current)
        val snapshot = current.toSnapshot()
        if (alreadyPlaced) {
            analytics.track(
                WidgetUpdated(
                    snapshot = snapshot,
                    from = (appliedSource ?: current.source).toAnalytics(),
                    to = current.source.toAnalytics(),
                ),
            )
        } else {
            analytics.track(WidgetAdded(snapshot))
        }
        publishState { copy(hasPendingChanges = false) }
    }

    override fun onSourceSelected(source: WidgetSource) {
        publishState { copy(selectedSource = source) }
        persist { setSource(source) }
    }

    override fun onCreatePlaylistClicked() = publishEffect(WidgetSettingsEffect.OpenCreatePlaylist)

    override fun onEditPlaylistClicked(playlistId: String) =
        publishEffect(WidgetSettingsEffect.OpenEditPlaylist(playlistId))

    override fun onDailyQuoteToggled(enabled: Boolean) {
        publishState { copy(includeDailyQuote = enabled) }
        persist { setDailyQuote(enabled) }
    }

    override fun onFrequencyClicked() =
        publishEffect(WidgetSettingsEffect.OpenFrequencyPicker(uiState.value.frequencyHours))

    override fun onStyleSelected(style: WidgetStyle) {
        publishState { copy(style = style) }
        persist { setStyle(style) }
    }

    override fun onEditStyleClicked(style: WidgetStyle) =
        publishEffect(WidgetSettingsEffect.OpenAppearanceEditor(style))

    override fun onFeedbackSelected(feedback: WidgetFeedback) =
        publishEffect(WidgetSettingsEffect.OpenFeedback(liked = feedback == WidgetFeedback.LIKE))

    override fun onAddToHomeClicked() = publishEffect(WidgetSettingsEffect.WidgetAdded)

    override fun onBackClicked(widgetPlaced: Boolean) = publishEffect(
        if (widgetPlaced && uiState.value.hasPendingChanges) {
            WidgetSettingsEffect.ConfirmExit
        } else {
            WidgetSettingsEffect.Close
        },
    )

    fun onExitAction(action: WidgetExitAction) = publishEffect(
        when (action) {
            WidgetExitAction.APPLY -> WidgetSettingsEffect.ApplyAndClose
            WidgetExitAction.LEAVE -> WidgetSettingsEffect.Close
        },
    )

    fun applyFrequency(hours: Int) {
        publishState { copy(frequencyHours = hours) }
        persist { setFrequency(hours) }
    }
}
