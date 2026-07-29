package com.kovhan.feature.widget.glance

import com.kovhan.domain.widget.use_case.content.GetWidgetQuoteUseCase
import com.kovhan.domain.widget.use_case.content.RebuildWidgetSnapshotUseCase
import com.kovhan.domain.widget.use_case.content.RotateWidgetQuoteUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getWidgetQuote(): GetWidgetQuoteUseCase
    fun rotateWidgetQuote(): RotateWidgetQuoteUseCase
    fun rebuildWidgetSnapshot(): RebuildWidgetSnapshotUseCase
    fun observeWidgetSettings(): ObserveWidgetSettingsUseCase
}
