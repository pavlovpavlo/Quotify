package com.kovhan.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kovhan.core.datastore.put
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.domain.widget.WidgetSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetSettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : WidgetSettingsRepository {

    override fun observe(): Flow<WidgetSettings> =
        dataStore.data.map { prefs ->
            WidgetSettings(
                source = decodeSource(prefs[SettingsPreferences.Widget.SOURCE]),
                includeDailyQuote = prefs[SettingsPreferences.Widget.INCLUDE_DAILY_QUOTE] ?: true,
                frequencyHours = prefs[SettingsPreferences.Widget.FREQUENCY_HOURS]
                    ?: WidgetSettings.DEFAULT_FREQUENCY_HOURS,
                style = decodeStyle(prefs[SettingsPreferences.Widget.STYLE]),
            )
        }

    override suspend fun setSource(source: WidgetSource) =
        dataStore.put(SettingsPreferences.Widget.SOURCE, encodeSource(source))

    override suspend fun setIncludeDailyQuote(enabled: Boolean) =
        dataStore.put(SettingsPreferences.Widget.INCLUDE_DAILY_QUOTE, enabled)

    override suspend fun setFrequencyHours(hours: Int) =
        dataStore.put(
            SettingsPreferences.Widget.FREQUENCY_HOURS,
            hours.coerceIn(
                WidgetSettings.MIN_FREQUENCY_HOURS,
                WidgetSettings.MAX_FREQUENCY_HOURS,
            ),
        )

    override suspend fun setStyle(style: WidgetStyle) =
        dataStore.put(SettingsPreferences.Widget.STYLE, style.name)

    private fun encodeSource(source: WidgetSource): String = when (source) {
        WidgetSource.All -> SOURCE_ALL
        WidgetSource.Favorites -> SOURCE_FAVORITES
        is WidgetSource.Playlist -> PLAYLIST_PREFIX + source.playlistId
    }

    private fun decodeSource(stored: String?): WidgetSource = when {
        stored == null || stored == SOURCE_ALL -> WidgetSource.All
        stored == SOURCE_FAVORITES -> WidgetSource.Favorites
        stored.startsWith(PLAYLIST_PREFIX) ->
            WidgetSource.Playlist(stored.removePrefix(PLAYLIST_PREFIX))
        else -> WidgetSource.All
    }

    private fun decodeStyle(stored: String?): WidgetStyle =
        stored?.let { runCatching { WidgetStyle.valueOf(it) }.getOrNull() } ?: WidgetStyle.CLASSIC

    private companion object {
        const val SOURCE_ALL = "all"
        const val SOURCE_FAVORITES = "favorites"
        const val PLAYLIST_PREFIX = "playlist:"
    }
}
