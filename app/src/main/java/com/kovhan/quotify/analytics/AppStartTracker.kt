package com.kovhan.quotify.analytics

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.AppStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStartTracker @Inject constructor(
    private val tracker: AnalyticsTracker,
    private val profileCollector: AnalyticsProfileCollector,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun onProcessStarted() {
        scope.launch {
            withTimeoutOrNull(PROFILE_TIMEOUT_MS) {
                runCatching { profileCollector.refresh() }
            }
            tracker.track(AppStart)
        }
    }

    private companion object {
        const val PROFILE_TIMEOUT_MS = 5_000L
    }
}
