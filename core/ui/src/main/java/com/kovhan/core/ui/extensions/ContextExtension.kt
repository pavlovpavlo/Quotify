package com.kovhan.core.ui.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.internal.managers.FragmentComponentManager

fun Context.findLifecycleOwner(): LifecycleOwner? {
    return FragmentComponentManager.findActivity(this) as? LifecycleOwner
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

/** Сторінка застосунку в Play Store; якщо маркета немає — та сама сторінка в браузері. */
fun Context.openAppInStore() {
    try {
        startActivity(storeIntent("market://details?id=$packageName"))
    } catch (e: ActivityNotFoundException) {
        startActivity(storeIntent("https://play.google.com/store/apps/details?id=$packageName"))
    }
}

private fun Context.storeIntent(url: String): Intent =
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        if (this@storeIntent !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
