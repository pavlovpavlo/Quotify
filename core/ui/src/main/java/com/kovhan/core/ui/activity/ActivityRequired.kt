package com.kovhan.core.ui.activity

import androidx.fragment.app.FragmentActivity

interface ActivityRequired {

    fun onCreated(activity: FragmentActivity)

    fun onStarted()

    fun onStopped()

    fun onDestroyed()

    fun onResumed() {}
}
