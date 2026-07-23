package com.kovhan.core.ui.snackbar

import kotlinx.coroutines.flow.Flow

interface SnackbarMessageSource {
    val messages: Flow<SnackbarMessage>
}
