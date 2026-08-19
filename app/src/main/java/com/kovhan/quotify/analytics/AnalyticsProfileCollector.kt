package com.kovhan.quotify.analytics

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.kovhan.core.analytics.AnalyticsProfile
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.AppTheme as AnalyticsTheme
import com.kovhan.core.analytics.AuthType
import com.kovhan.core.analytics.SubscriptionState
import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.use_case.GetUserUseCase
import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.library.use_case.collection.GetCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.GetQuotesUseCase
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.feature.widget.glance.QuotifyGlanceWidgetReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsProfileCollector @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tracker: AnalyticsTracker,
    private val getUser: GetUserUseCase,
    private val isSubscribed: IsSubscribedUseCase,
    private val getTheme: GetThemeUseCase,
    private val getLanguage: GetLanguageUseCase,
    private val getCollections: GetCollectionsUseCase,
    private val getQuotes: GetQuotesUseCase,
) {

    suspend fun refresh() {
        val user = runCatching { getUser().firstOrNull() }.getOrNull()
        val subscribed = runCatching { isSubscribed() }.getOrDefault(false)
        val theme = runCatching { getTheme().firstOrNull() }.getOrNull() ?: AppTheme.SYSTEM
        val language = runCatching { getLanguage().firstOrNull() }.getOrNull() ?: AppLanguage.default
        val collections = runCatching { getCollections() }.getOrDefault(emptyList())
        val quotes = runCatching { getQuotes() }.getOrDefault(emptyList())

        tracker.setUserId(user?.uid)
        tracker.updateProfile(
            AnalyticsProfile(
                lifeDays = lifeDays(),
                subscription = if (subscribed) SubscriptionState.PREMIUM else SubscriptionState.FREE,
                theme = theme.toAnalytics(),
                language = language.tag,
                collectionCount = collections.size,
                quoteCount = quotes.size,
                hasWidget = hasWidget(),
                authType = user.toAuthType(),
            ),
        )
    }

    private fun lifeDays(): Int {
        val installedAt = runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
        }.getOrDefault(0L)
        if (installedAt <= 0L) return 0
        val elapsed = System.currentTimeMillis() - installedAt
        return TimeUnit.MILLISECONDS.toDays(elapsed).toInt().coerceAtLeast(0)
    }

    private fun hasWidget(): Boolean = runCatching {
        AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, QuotifyGlanceWidgetReceiver::class.java))
            .isNotEmpty()
    }.getOrDefault(false)

    private fun AppTheme.toAnalytics(): AnalyticsTheme = when (this) {
        AppTheme.DARK -> AnalyticsTheme.DARK
        AppTheme.LIGHT -> AnalyticsTheme.LIGHT
        AppTheme.SYSTEM -> if (context.isSystemInDarkTheme()) {
            AnalyticsTheme.DARK
        } else {
            AnalyticsTheme.LIGHT
        }
    }

    private fun Context.isSystemInDarkTheme(): Boolean =
        (
            resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
            ) == android.content.res.Configuration.UI_MODE_NIGHT_YES

    private fun AuthUser?.toAuthType(): AuthType = when {
        this == null || isAnonymous -> AuthType.ANONYMOUS
        isGoogleAccount -> AuthType.GOOGLE
        else -> AuthType.EMAIL
    }
}
