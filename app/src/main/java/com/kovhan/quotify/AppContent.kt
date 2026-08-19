package com.kovhan.quotify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfferKey
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.core.navigation.PlaceholderBottomSheetKey
import com.kovhan.core.navigation.PlaceholderDialogKey
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.SplashKey
import com.kovhan.core.ui.snackbar.QuotifySnackbar
import com.kovhan.domain.premium.OfferTrigger
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import com.kovhan.quotify.navigation.dock.BottomDock
import kotlinx.coroutines.flow.Flow

private val SnackbarBottomInset = 16.dp
private val SnackbarDockInset = 90.dp

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    uiState: MainActivityState,
    uiIntent: MainIntent,
    coordinator: NavigationCoordinator,
    entryBuilders: Set<EntryBuilder>,
    bottomSheetEntryBuilders: Set<BottomSheetEntryBuilder>,
    dialogEntryBuilders: Set<DialogEntryBuilder>,
    snackbarMessages: Flow<SnackbarMessage>,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(snackbarMessages, snackbarHostState)

    LaunchedEffect(Unit) {
        if (coordinator.backStack.isEmpty()) {
            coordinator.initialize(SplashKey)
        }
    }

    LaunchedEffect(coordinator.pendingDeepLink, coordinator.currentKey) {
        val pending = coordinator.pendingDeepLink ?: return@LaunchedEffect
        val reachedMain = coordinator.backStack.any { coordinator.isTopLevelKey(it) }
        if (reachedMain) {
            coordinator.pendingDeepLink = null
            coordinator.navigate(QuotesKey)
            coordinator.navigate(pending)
        }
    }

    LaunchedEffect(uiState.pendingOfferTrigger, coordinator.currentKey) {
        if (uiState.pendingOfferTrigger == null) return@LaunchedEffect
        val current = coordinator.currentKey ?: return@LaunchedEffect
        if (!coordinator.isTopLevelKey(current)) return@LaunchedEffect

        val origin = when (uiState.pendingOfferTrigger) {
            OfferTrigger.CANCELED -> PaywallOrigin.CANCEL
            OfferTrigger.TENURE -> PaywallOrigin.TENURE
            null -> PaywallOrigin.BANNER
        }
        uiIntent.onOfferShown()
        coordinator.navigate(OfferKey(origin))
    }

    val showDock by remember {
        derivedStateOf { coordinator.shouldShowBottomBar() }
    }

    LaunchedEffect(showDock) {
        uiIntent.onDockVisibilityChanged(showDock)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = modifier.fillMaxSize()) {
            if (coordinator.backStack.isNotEmpty()) {
                NavDisplay(
                    backStack = coordinator.backStack,
                    onBack = { coordinator.goBack() },
                    entryProvider = entryProvider {
                        entryBuilders.forEach { builder ->
                            builder.build(this, coordinator, innerPadding)
                        }
                    },
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    predictivePopTransitionSpec = {
                        EnterTransition.None togetherWith ExitTransition.None
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    modifier = Modifier.fillMaxSize(),
                )

                NavDisplay(
                    backStack = coordinator.bottomSheetStack,
                    onBack = { coordinator.dismissBottomSheet() },
                    entryProvider = entryProvider {
                        entry<PlaceholderBottomSheetKey> { }
                        bottomSheetEntryBuilders.forEach { builder ->
                            builder.build(this, coordinator, PaddingValues())
                        }
                    },
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    predictivePopTransitionSpec = {
                        EnterTransition.None togetherWith ExitTransition.None
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                )

                NavDisplay(
                    backStack = coordinator.dialogStack,
                    onBack = { coordinator.dismissDialog() },
                    entryProvider = entryProvider {
                        entry<PlaceholderDialogKey> { }
                        dialogEntryBuilders.forEach { builder ->
                            builder.build(this, coordinator, PaddingValues())
                        }
                    },
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    predictivePopTransitionSpec = {
                        EnterTransition.None togetherWith ExitTransition.None
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                )

                AnimatedVisibility(
                    visible = showDock,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    BottomDock(
                        coordinator = coordinator,
                        isFabTooltipVisible = uiState.isFabTooltipVisible,
                        uiIntent = uiIntent,
                    )
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(bottom = if (showDock) SnackbarDockInset else SnackbarBottomInset),
            ) { data ->
                QuotifySnackbar(snackbarData = data)
            }
        }
    }
}
