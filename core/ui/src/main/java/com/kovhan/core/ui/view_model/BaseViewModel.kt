package com.kovhan.core.ui.view_model

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.mvi.MVI
import com.kovhan.core.ui.mvi.ModelViewIntent
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Effect : UiEffect>(
    initialState: State
) : ViewModel(),
    MVI<State, Effect> by ModelViewIntent(initialState) {

    private val snackbarChannel = Channel<SnackbarMessage>(Channel.BUFFERED)

    val snackbar: Flow<SnackbarMessage> = snackbarChannel.receiveAsFlow()

    protected fun showSnackbar(message: SnackbarMessage) {
        viewModelScope.launch { snackbarChannel.send(message) }
    }

    protected fun showSnackbar(
        @StringRes messageRes: Int,
        type: SnackbarType = SnackbarType.Info,
    ) = showSnackbar(SnackbarMessage(messageRes = messageRes, type = type))


    val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    override fun onCleared() {
        super.onCleared()
        snackbarChannel.close()
        cancelMviScope()
        viewModelScope.cancel()
    }
}
