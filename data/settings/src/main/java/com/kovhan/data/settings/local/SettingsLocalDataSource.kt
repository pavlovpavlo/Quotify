package com.kovhan.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kovhan.core.datastore.get
import com.kovhan.core.datastore.getOnes
import com.kovhan.core.datastore.put
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.premium.OfferPromptRepository
import com.kovhan.domain.premium.OfferTrigger
import com.kovhan.domain.premium.PaywallPromptRepository
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository, OnboardingRepository, PaywallPromptRepository, OfferPromptRepository {

    override fun observeTheme(): Flow<AppTheme> =
        dataStore.get(SettingsPreferences.Settings.THEME).map { stored ->
            stored?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() } ?: AppTheme.SYSTEM
        }

    override suspend fun setTheme(theme: AppTheme) =
        dataStore.put(SettingsPreferences.Settings.THEME, theme.name)

    override fun observeLanguage(): Flow<AppLanguage> =
        dataStore.get(SettingsPreferences.Settings.LANGUAGE).map { AppLanguage.fromTag(it) }

    override suspend fun setLanguage(language: AppLanguage) =
        dataStore.put(SettingsPreferences.Settings.LANGUAGE, language.tag)

    override fun observeDailyQuoteEnabled(): Flow<Boolean> =
        dataStore.get(SettingsPreferences.Settings.DAILY_QUOTE_ENABLED, true)

    override suspend fun setDailyQuoteEnabled(enabled: Boolean) =
        dataStore.put(SettingsPreferences.Settings.DAILY_QUOTE_ENABLED, enabled)

    override fun isCompleted(): Flow<Boolean> =
        dataStore.get(SettingsPreferences.Onboarding.COMPLETED, false)

    override suspend fun setCompleted(completed: Boolean) =
        dataStore.put(SettingsPreferences.Onboarding.COMPLETED, completed)

    override fun isFabTooltipDismissed(): Flow<Boolean> =
        dataStore.get(SettingsPreferences.Onboarding.FAB_TOOLTIP_DISMISSED, false)

    override suspend fun setFabTooltipDismissed(dismissed: Boolean) =
        dataStore.put(SettingsPreferences.Onboarding.FAB_TOOLTIP_DISMISSED, dismissed)

    override suspend fun lastShownAt(): Long =
        dataStore.getOnes(SettingsPreferences.Paywall.LAST_SHOWN_AT, 0L)

    override suspend fun markShown(timestamp: Long) =
        dataStore.put(SettingsPreferences.Paywall.LAST_SHOWN_AT, timestamp)

    override suspend fun freeSinceAt(): Long =
        dataStore.getOnes(SettingsPreferences.Offer.FREE_SINCE_AT, 0L)

    override suspend fun rememberFreeSince(timestamp: Long) =
        dataStore.put(SettingsPreferences.Offer.FREE_SINCE_AT, timestamp)

    override suspend fun isShown(trigger: OfferTrigger): Boolean =
        dataStore.getOnes(trigger.shownKey(), false)

    override suspend fun markShown(trigger: OfferTrigger) =
        dataStore.put(trigger.shownKey(), true)

    override suspend fun clearShown(trigger: OfferTrigger) =
        dataStore.put(trigger.shownKey(), false)

    private fun OfferTrigger.shownKey(): Preferences.Key<Boolean> = when (this) {
        OfferTrigger.CANCELED -> SettingsPreferences.Offer.SHOWN_CANCELED
        OfferTrigger.TENURE -> SettingsPreferences.Offer.SHOWN_TENURE
    }
}
