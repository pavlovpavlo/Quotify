package com.kovhan.core.ui.snackbar.di

import com.kovhan.core.ui.snackbar.AppSnackbarBus
import com.kovhan.core.ui.snackbar.SnackbarDispatcher
import com.kovhan.core.ui.snackbar.SnackbarMessageSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SnackbarModule {

    @Provides
    fun provideSnackbarDispatcher(): SnackbarDispatcher = AppSnackbarBus

    @Provides
    fun provideSnackbarMessageSource(): SnackbarMessageSource = AppSnackbarBus
}
