package com.kovhan.quotify.di

import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.feature.addquote.navigation.AddQuoteBottomSheetEntryBuilder
import com.kovhan.feature.entitydetails.navigation.EntityDetailsBottomSheetEntryBuilder
import com.kovhan.feature.main.navigation.MainBottomSheetEntryBuilder
import com.kovhan.feature.splash.navigation.SplashBottomSheetEntryBuilder
import com.kovhan.feature.widget.navigation.WidgetBottomSheetEntryBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class BottomSheetNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindMainBottomSheetEntryBuilder(
        impl: MainBottomSheetEntryBuilder,
    ): BottomSheetEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindEntityDetailsBottomSheetEntryBuilder(
        impl: EntityDetailsBottomSheetEntryBuilder,
    ): BottomSheetEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindAddQuoteBottomSheetEntryBuilder(
        impl: AddQuoteBottomSheetEntryBuilder,
    ): BottomSheetEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindSplashBottomSheetEntryBuilder(
        impl: SplashBottomSheetEntryBuilder,
    ): BottomSheetEntryBuilder

    @Binds
    @IntoSet
    abstract fun bindWidgetBottomSheetEntryBuilder(
        impl: WidgetBottomSheetEntryBuilder,
    ): BottomSheetEntryBuilder
}
