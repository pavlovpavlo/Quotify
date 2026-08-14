package com.kovhan.quotify.mvi

import com.kovhan.quotify.navigation.dock.mvi.DockIntent

interface MainIntent : DockIntent {

    fun onAppForegrounded()

    fun onOfferShown()
}
