package com.kovhan.core.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsTracker {

    override fun track(event: AnalyticsEvent) {
        val bundle = event.params.toBundle()
        Timber.d("Analytics: %s %s", event.name, event.params)
        firebaseAnalytics.logEvent(event.name, bundle)
    }

    override fun updateProfile(profile: AnalyticsProfile) {
        firebaseAnalytics.setDefaultEventParameters(
            mapOf<String, Any>(
                AnalyticsParam.LIFE_DAYS to profile.lifeDays,
                AnalyticsParam.SUBSCRIPTION to profile.subscription.value,
            ).toBundle(),
        )

        with(firebaseAnalytics) {
            setUserProperty(AnalyticsParam.SUBSCRIPTION, profile.subscription.value)
            setUserProperty(AnalyticsParam.LIFE_DAYS, profile.lifeDays.toString())
            setUserProperty(AnalyticsParam.THEME, profile.theme.value)
            setUserProperty(AnalyticsParam.LANGUAGE, profile.language)
            setUserProperty(AnalyticsParam.COLLECTION_COUNT, profile.collectionCount.toString())
            setUserProperty(AnalyticsParam.QUOTE_COUNT, profile.quoteCount.toString())
            setUserProperty(AnalyticsParam.HAS_WIDGET, profile.hasWidget.toString())
            setUserProperty(AnalyticsParam.AUTH_TYPE, profile.authType.value)
        }
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    private fun Map<String, Any>.toBundle(): Bundle {
        val bundle = Bundle()
        forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putLong(key, value.toLong())
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putDouble(key, value.toDouble())
                is Boolean -> bundle.putString(key, value.toString())
                else -> bundle.putString(key, value.toString())
            }
        }
        return bundle
    }
}
