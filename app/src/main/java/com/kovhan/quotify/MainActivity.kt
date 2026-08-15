package com.kovhan.quotify

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.kovhan.core.ui.activity.ActivityRequired
import com.kovhan.core.ui.locale.LocalAppLocaleContext
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_QUOTE
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_SETTINGS
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WidgetQuoteKey
import com.kovhan.core.navigation.WidgetSettingsKey
import com.kovhan.core.ui.util.ContextUtils
import com.kovhan.design.systems.QuotifyAppTheme
import com.kovhan.feature.widget.glance.WidgetRefreshWorker
import com.kovhan.domain.settings.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var activityRequired: Set<@JvmSuppressWildcards ActivityRequired>

    @Inject
    lateinit var navigationCoordinator: NavigationCoordinator

    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryBuilder>

    @Inject
    lateinit var bottomSheetEntryBuilders: Set<@JvmSuppressWildcards BottomSheetEntryBuilder>

    @Inject
    lateinit var dialogEntryBuilders: Set<@JvmSuppressWildcards DialogEntryBuilder>

    // Driven from onConfigurationChanged so a live system dark-mode toggle updates
    // the theme. Compose's isSystemInDarkTheme() misses it because the custom
    // createConfigurationContext base context detaches from system config updates.
    private val systemInDarkTheme = mutableStateOf(false)

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(ContextUtils.updateConfiguration(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        systemInDarkTheme.value = newConfig.isSystemInDarkTheme()
    }

    private fun Configuration.isSystemInDarkTheme(): Boolean =
        (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        systemInDarkTheme.value = resources.configuration.isSystemInDarkTheme()

        handleWidgetDeepLink(intent)

        // The library fills in after the remote sync, so redraw once the app is
        // up — otherwise a widget placed earlier sits on a stale empty state.
        WidgetRefreshWorker.enqueue(applicationContext)

        activityRequired.forEach { it.onCreated(this) }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isDarkTheme = when (uiState.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> systemInDarkTheme.value
            }

            val baseContext = LocalContext.current
            val localizedContext = remember(baseContext, uiState.language) {
                val configuration = Configuration(baseContext.resources.configuration).apply {
                    setLocale(Locale(uiState.language.tag))
                }
                ContextThemeWrapper(baseContext, 0).apply {
                    applyOverrideConfiguration(configuration)
                }
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides localizedContext.resources.configuration,
                LocalAppLocaleContext provides localizedContext,
                LocalNavigationEventDispatcherOwner provides this@MainActivity,
            ) {
                QuotifyAppTheme(
                    activity = this,
                    isDarkTheme = isDarkTheme,
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        contentColor = Color.Transparent,
                        color = Color.Transparent,
                    ) {
                        AppContent(
                            uiState = uiState,
                            uiIntent = viewModel,
                            coordinator = navigationCoordinator,
                            entryBuilders = entryBuilders,
                            bottomSheetEntryBuilders = bottomSheetEntryBuilders,
                            dialogEntryBuilders = dialogEntryBuilders,
                            snackbarMessages = viewModel.snackbarMessages,
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleWidgetDeepLink(intent)
    }

    private fun handleWidgetDeepLink(intent: Intent?) {
        intent ?: return
        val quoteId = intent.getStringExtra(EXTRA_OPEN_WIDGET_QUOTE)
        when {
            !quoteId.isNullOrBlank() ->
                navigationCoordinator.pendingDeepLink = WidgetQuoteKey(quoteId)

            intent.getBooleanExtra(EXTRA_OPEN_WIDGET_SETTINGS, false) ->
                navigationCoordinator.pendingDeepLink = WidgetSettingsKey
        }
    }

    override fun onStart() {
        super.onStart()
        activityRequired.forEach { it.onStarted() }
        viewModel.onAppForegrounded()
    }

    override fun onResume() {
        super.onResume()
        activityRequired.forEach { it.onResumed() }
    }

    override fun onStop() {
        activityRequired.forEach { it.onStopped() }
        super.onStop()
    }

    override fun onDestroy() {
        activityRequired.forEach { it.onDestroyed() }
        super.onDestroy()
    }
}
