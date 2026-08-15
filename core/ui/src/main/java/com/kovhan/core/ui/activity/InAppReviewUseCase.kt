package com.kovhan.core.ui.activity

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Play's in-app review overlay. Google throttles it per user and per app, so a
 * request can silently do nothing — callers must treat "asked" as best effort
 * and never block on a result.
 */
@Singleton
class InAppReviewUseCase @Inject constructor() : ActivityRequired {

    private var activity: Activity? = null

    operator fun invoke() {
        val current = activity ?: return
        val manager = ReviewManagerFactory.create(current)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (!request.isSuccessful) {
                Timber.e(request.exception, "Review: failed to request flow")
                return@addOnCompleteListener
            }
            manager.launchReviewFlow(current, request.result)
                .addOnFailureListener { Timber.e(it, "Review: failed to launch flow") }
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        this.activity = activity
    }

    override fun onStarted() = Unit

    override fun onStopped() = Unit

    override fun onDestroyed() {
        activity = null
    }
}
