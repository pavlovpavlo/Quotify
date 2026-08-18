package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.kovhan.core.ui.widget.HomeWidgetPresence
import com.kovhan.domain.widget.WidgetRedrawer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlanceWidgetRedrawer @Inject constructor(
    @ApplicationContext private val context: Context,
) : WidgetRedrawer {

    override suspend fun redraw() {
        if (!HomeWidgetPresence.isPlaced(context)) return
        runCatching { QuotifyGlanceWidget().updateAll(context) }
            .onFailure { Timber.e(it, "Widget: failed to redraw") }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WidgetRedrawerModule {

    @Binds
    abstract fun bindWidgetRedrawer(impl: GlanceWidgetRedrawer): WidgetRedrawer
}
