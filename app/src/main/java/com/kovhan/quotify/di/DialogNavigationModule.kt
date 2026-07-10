package com.kovhan.quotify.di

import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.feature.addquote.navigation.AddQuoteDialogEntryBuilder
import com.kovhan.feature.entitydetails.navigation.EntityDetailsDialogEntryBuilder
import com.kovhan.feature.main.navigation.MainDialogEntryBuilder
import com.kovhan.feature.splash.navigation.SplashDialogEntryBuilder
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
    abstract fun bindAddQuoteDialogEntryBuilder(impl: AddQuoteDialogEntryBuilder): DialogEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindMainDialogEntryBuilder(impl: MainDialogEntryBuilder): DialogEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindEntityDetailsDialogEntryBuilder(
        impl: EntityDetailsDialogEntryBuilder,
    ): DialogEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindSplashDialogEntryBuilder(impl: SplashDialogEntryBuilder): DialogEntryBuilder
}
