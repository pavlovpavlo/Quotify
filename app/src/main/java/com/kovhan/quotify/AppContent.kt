package com.kovhan.quotify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kovhan.core.ui.component.BottomBar
import com.kovhan.core.ui.extensions.findLifecycleOwner
import com.kovhan.feature.splash.navigation.Splash
import com.kovhan.feature.splash.navigation.splashGraph
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    uiState: MainActivityState,
    uiIntent: MainIntent,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val localDensity = LocalDensity.current
    val lifecycleOwner = remember { context.findLifecycleOwner() }
    val snackBarSuccessHostState = remember { SnackbarHostState() }
    val snackBarErrorHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            snackBarSuccessHostState.currentSnackbarData?.dismiss()
            snackBarErrorHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController
            )
        },
        //containerColor = HasherMaterialTheme.colors.backgroundColor
    ) { innerPaddingModifier ->

        LaunchedEffect(innerPaddingModifier.calculateBottomPadding()) {
//            val bottomPadding = localDensity.run {
//                innerPaddingModifier.calculateBottomPadding().toPx()
//            }.toInt()
//            uiIntent.updateChatBottomPadding(bottomPadding)
        }

        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navController,
            startDestination = Splash,
        ) {
            splashGraph(navController, paddingValues = innerPaddingModifier,
                navigateToOnboarding = {
                    navController
                },
                navigateToMain = {

                })
        }
    }
}