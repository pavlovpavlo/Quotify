package com.kovhan.quotify.navigation.dock.mvi

interface DockIntent {
    fun onFabClicked()

    fun onDockVisibilityChanged(visible: Boolean)
}
