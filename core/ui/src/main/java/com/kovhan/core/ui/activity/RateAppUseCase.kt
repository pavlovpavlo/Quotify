package com.kovhan.core.ui.activity

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.kovhan.core.ui.extensions.openAppInStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateAppUseCase @Inject constructor() : ActivityRequired {

    private var activity: Activity? = null

    operator fun invoke() {
        activity?.openAppInStore()
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
