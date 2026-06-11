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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaceholderBottomSheetKey
import com.kovhan.core.navigation.PlaceholderDialogKey
import com.kovhan.core.navigation.SplashKey
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import com.kovhan.quotify.navigation.dock.BottomDock

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    uiState: MainActivityState,
    uiIntent: MainIntent,
    coordinator: NavigationCoordinator,
    entryBuilders: Set<EntryBuilder>,
    bottomSheetEntryBuilders: Set<BottomSheetEntryBuilder>,
    dialogEntryBuilders: Set<DialogEntryBuilder>,
) {
    LaunchedEffect(Unit) {
        if (coordinator.backStack.isEmpty()) {
            coordinator.initialize(SplashKey)
        }
    }

    val showDock by remember {
        derivedStateOf { coordinator.shouldShowBottomBar() }
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
                    BottomDock(coordinator = coordinator)
                }
            }
        }
    }
}
