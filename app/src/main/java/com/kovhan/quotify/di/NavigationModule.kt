package com.kovhan.quotify.di

import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.feature.addquote.navigation.AddQuoteEntryBuilder
import com.kovhan.feature.auth.navigation.AuthEntryBuilder
import com.kovhan.feature.main.navigation.MainEntryBuilder
import com.kovhan.feature.onboarding.navigation.OnboardingEntryBuilder
import com.kovhan.feature.splash.navigation.SplashEntryBuilder
import com.kovhan.feature.webview.navigation.WebViewEntryBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

/**
 * Collects every feature's screen [EntryBuilder] via Hilt multibinding and
 * exposes them as a `Set<EntryBuilder>` injected into MainActivity.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class NavigationModule {

    @Binds
    @IntoSet
    abstract fun bindSplashEntryBuilder(impl: SplashEntryBuilder): EntryBuilder

    @Binds
    @IntoSet
    abstract fun bindOnboardingEntryBuilder(impl: OnboardingEntryBuilder): EntryBuilder

    @Binds
    @IntoSet
    abstract fun bindAuthEntryBuilder(impl: AuthEntryBuilder): EntryBuilder

    @Binds
    @IntoSet
    abstract fun bindMainEntryBuilder(impl: MainEntryBuilder): EntryBuilder

    @Binds
    @IntoSet
    abstract fun bindWebViewEntryBuilder(impl: WebViewEntryBuilder): EntryBuilder

    @Binds
    @IntoSet
    abstract fun bindAddQuoteEntryBuilder(impl: AddQuoteEntryBuilder): EntryBuilder
}
