package com.kovhan.quotify

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kovhan.core.ui.activity.ActivityRequired
import com.kovhan.core.ui.util.ContextUtils
import com.kovhan.design.systems.QuotifyAppTheme
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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(ContextUtils.updateConfiguration(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityRequired.forEach { it.onCreated(this) }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }
        setContent {
            val navController = rememberNavController()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isDarkTheme = when (uiState.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
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
                            navController = navController,
                            uiState = uiState,
                            uiIntent = viewModel,
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activityRequired.forEach { it.onStarted() }
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
