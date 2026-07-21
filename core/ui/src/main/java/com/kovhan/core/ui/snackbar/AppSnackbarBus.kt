package com.kovhan.core.ui.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Process-wide delivery point for snackbar messages: any ViewModel dispatches here,
 * the single host in AppContent consumes.
 */
object AppSnackbarBus : SnackbarDispatcher, SnackbarMessageSource {

    private val channel = Channel<SnackbarMessage>(Channel.BUFFERED)

    override val messages: Flow<SnackbarMessage> = channel.receiveAsFlow()

    override fun show(message: SnackbarMessage) {
        channel.trySend(message)
    }
}
