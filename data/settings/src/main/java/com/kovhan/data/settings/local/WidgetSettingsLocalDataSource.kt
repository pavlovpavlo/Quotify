package com.kovhan.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kovhan.core.datastore.get
import com.kovhan.core.datastore.put
import com.kovhan.core.models.widget.WidgetAppearance
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.data.settings.dto.WidgetAppearanceDto
import com.kovhan.data.settings.mapper.toDomain
import com.kovhan.data.settings.mapper.toDto
import com.kovhan.domain.widget.WidgetSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
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
                appearance = decodeAppearance(prefs[SettingsPreferences.Widget.APPEARANCE]),
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

    override suspend fun setStyleSettings(settings: WidgetStyleSettings) {
        val updated = observe().first().appearance.with(settings)
        dataStore.put(
            SettingsPreferences.Widget.APPEARANCE,
            json.encodeToString(WidgetAppearanceDto.serializer(), updated.toDto()),
        )
    }

    override suspend fun isApplied(settings: WidgetSettings): Boolean =
        dataStore.get(SettingsPreferences.Widget.APPLIED_SNAPSHOT).first() == snapshot(settings)

    override suspend fun appliedSource(): WidgetSource? =
        dataStore.get(SettingsPreferences.Widget.APPLIED_SOURCE).first()?.let(::decodeSource)

    override suspend fun rememberApplied(settings: WidgetSettings) {
        dataStore.put(SettingsPreferences.Widget.APPLIED_SNAPSHOT, snapshot(settings))
        dataStore.put(SettingsPreferences.Widget.APPLIED_SOURCE, encodeSource(settings.source))
    }

    /**
     * Only what the widget actually draws: the appearance of the other two styles
     * can change without the home screen ever looking different.
     */
    private fun snapshot(settings: WidgetSettings): String = listOf(
        encodeSource(settings.source),
        settings.includeDailyQuote.toString(),
        settings.frequencyHours.toString(),
        settings.style.name,
        settings.activeStyleSettings.toString(),
    ).joinToString("|")

    private fun decodeAppearance(stored: String?): WidgetAppearance {
        val dto = stored
            ?.let {
                runCatching { json.decodeFromString(WidgetAppearanceDto.serializer(), it) }
                    .getOrNull()
            }
            ?: WidgetAppearanceDto()
        return dto.toDomain()
    }

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

        val json = Json { ignoreUnknownKeys = true }
    }
}
