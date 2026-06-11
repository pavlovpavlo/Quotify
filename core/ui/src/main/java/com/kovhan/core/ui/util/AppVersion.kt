package com.kovhan.core.ui.util

import android.content.Context

fun Context.appVersionName(): String =
    runCatching { packageManager.getPackageInfo(packageName, 0).versionName }.getOrNull().orEmpty()
