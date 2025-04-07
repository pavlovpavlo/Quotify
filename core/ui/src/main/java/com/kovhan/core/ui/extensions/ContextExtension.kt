package com.kovhan.core.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
