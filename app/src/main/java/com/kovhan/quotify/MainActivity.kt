package com.kovhan.quotify

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kovhan.design.systems.QuotifyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }
        setContent {
            val navController = rememberNavController()
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
            val isDarkTheme = uiState.isDarkMode || isSystemInDarkTheme()

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
