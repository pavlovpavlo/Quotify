package com.kovhan.core.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateAppUseCase @Inject constructor() : ActivityRequired {

    private var activity: Activity? = null

    operator fun invoke() {
        val current = activity ?: return
        val packageName = current.packageName
        val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        try {
            current.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            current.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
                ),
            )
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
