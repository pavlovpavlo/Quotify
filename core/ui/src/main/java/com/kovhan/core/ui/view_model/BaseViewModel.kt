package com.kovhan.core.ui.view_model

import androidx.lifecycle.ViewModel
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.mvi.MVI
import com.kovhan.core.ui.mvi.ModelViewIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel<State : UiState, Effect : UiEffect>(
    initialState: State
) : ViewModel(),
    MVI<State, Effect> by ModelViewIntent(initialState) {

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
        cancelMviScope()
        viewModelScope.cancel()
        //jobManager.cancelAll()
    }
}
