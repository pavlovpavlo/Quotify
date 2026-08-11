package com.kovhan.core.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.kovhan.core.ui.util.appVersionName
import com.kovhan.design.systems.R
import javax.inject.Inject
import javax.inject.Singleton

private const val SUPPORT_EMAIL = "lumadaystudio@gmail.com"

/** Opens the user's mail app with a prefilled message to support. */
@Singleton
class ContactSupportUseCase @Inject constructor() : ActivityRequired {

    private var activity: Activity? = null

    /** Returns false when the device has no app able to send mail. */
    operator fun invoke(): Boolean {
        val current = activity ?: return false
        val subject = current.getString(
            R.string.support_email_subject,
            current.getString(R.string.app_name),
            current.appVersionName(),
        )
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$SUPPORT_EMAIL")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        return try {
            current.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
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
