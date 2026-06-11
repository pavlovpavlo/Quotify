package com.kovhan.quotify.di

import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.feature.main.navigation.MainDialogEntryBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DialogNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindMainDialogEntryBuilder(impl: MainDialogEntryBuilder): DialogEntryBuilder
}
