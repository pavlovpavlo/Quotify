package com.kovhan.core.ui.snackbar

fun interface SnackbarDispatcher {
    fun show(message: SnackbarMessage)
}
