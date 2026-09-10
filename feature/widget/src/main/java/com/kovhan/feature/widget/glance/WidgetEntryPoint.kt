package com.kovhan.feature.widget.glance

import com.kovhan.domain.widget.use_case.content.EnsureWidgetQuoteUseCase
import com.kovhan.domain.widget.use_case.content.ObserveWidgetQuoteUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun ensureWidgetQuote(): EnsureWidgetQuoteUseCase
    fun observeWidgetSettings(): ObserveWidgetSettingsUseCase
    fun observeWidgetQuote(): ObserveWidgetQuoteUseCase
}
