package com.kovhan.core.ui.snackbar

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

enum class SnackbarType { Error, Success, Info }

data class SnackbarMessage(
    @param:StringRes val messageRes: Int? = null,
    val rawMessage: String? = null,
    val type: SnackbarType = SnackbarType.Info,
) {
    fun resolve(context: Context): String =
        rawMessage ?: messageRes?.let(context::getString).orEmpty()

    companion object {
        fun error(@StringRes res: Int) = SnackbarMessage(messageRes = res, type = SnackbarType.Error)
        fun success(@StringRes res: Int) = SnackbarMessage(messageRes = res, type = SnackbarType.Success)
        fun info(@StringRes res: Int) = SnackbarMessage(messageRes = res, type = SnackbarType.Info)
    }
}

class QuotifySnackbarVisuals(
    override val message: String,
    val type: SnackbarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals
