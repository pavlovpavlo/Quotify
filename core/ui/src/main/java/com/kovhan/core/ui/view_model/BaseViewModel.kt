package com.kovhan.core.ui.view_model

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.mvi.MVI
import com.kovhan.core.ui.mvi.ModelViewIntent
import com.kovhan.core.ui.snackbar.AppSnackbarBus
import com.kovhan.core.ui.snackbar.SnackbarDispatcher
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel<State : UiState, Effect : UiEffect>(
    initialState: State,
    private val snackbarDispatcher: SnackbarDispatcher = AppSnackbarBus,
) : ViewModel(),
    MVI<State, Effect> by ModelViewIntent(initialState) {

    protected fun showSnackbar(message: SnackbarMessage) = snackbarDispatcher.show(message)

    protected fun showSnackbar(
        @StringRes messageRes: Int,
        type: SnackbarType = SnackbarType.Info,
    ) = showSnackbar(SnackbarMessage(messageRes = messageRes, type = type))

    val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    override fun onCleared() {
        super.onCleared()
        cancelMviScope()
        viewModelScope.cancel()
    }
}
