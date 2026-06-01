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

//    @Inject
//    lateinit var alertManager: AlertManager
//
//    protected val jobManager: JobManager = JobManagerImpl()

    val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

//    init {
//        viewModelScope.launch {
//            errorStream.collect(::showErrorAlert)
//        }
//    }

//    protected open fun showErrorAlert(error: DataError) {
//        when (error) {
//            is DataError.Message -> viewModelScope.show(HasherAlert.Error.Message(error.message))
//            is DataError.Resource -> viewModelScope.show(HasherAlert.Error.Resource(error.messageId))
//        }
//    }
//
//    protected open fun showErrorAlert(@StringRes messageId: Int, vararg formatArgs: Any) {
//        viewModelScope.show(HasherAlert.Error.Resource(messageId, formatArgs.toList()))
//    }
//
//    protected open fun showSuccessAlert(@StringRes messageId: Int) {
//        viewModelScope.show(HasherAlert.Success(messageId))
//    }
//
//    protected open fun showCopyAlert(@StringRes messageId: Int) {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
//            viewModelScope.show(HasherAlert.Success(messageId))
//        }
//    }
//
//    private fun CoroutineScope.show(alert: HasherAlert) {
//        launch {
//            alertManager.showAlert(alert)
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        snackbarChannel.close()
        cancelMviScope()
        viewModelScope.cancel()
        //jobManager.cancelAll()
    }
}
