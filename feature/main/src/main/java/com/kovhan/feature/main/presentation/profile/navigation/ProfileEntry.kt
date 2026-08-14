package com.kovhan.feature.main.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.AboutKey
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.EditProfileKey
import com.kovhan.core.navigation.LanguageSheetKey
import com.kovhan.core.navigation.DevToolsKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.ReminderTimeSheetKey
import com.kovhan.core.navigation.SubscriptionKey
import com.kovhan.core.navigation.ThemeSheetKey
import com.kovhan.core.navigation.WidgetSettingsKey
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.feature.main.presentation.profile.ProfileScreen
import com.kovhan.feature.main.presentation.profile.ProfileScreenViewModel
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect

@Composable
internal fun ProfileEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<ProfileScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : ProfileScreenNavAction {
        override fun navigateBack() {
            coordinator.goBack()
        }

        override fun navigateToEditProfile() = coordinator.navigate(EditProfileKey)

        override fun navigateToAbout() = coordinator.navigate(AboutKey)

        override fun navigateToAuth() = coordinator.navigateAndClearBackStack(CompleteKey)

        override fun navigateToDevTools() = coordinator.navigate(DevToolsKey)
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<AppTheme>(NavigationCoordinator.KEY_SELECTED_THEME)
            .collect { theme -> theme?.let(viewModel::onThemeSelected) }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<AppLanguage>(NavigationCoordinator.KEY_SELECTED_LANGUAGE)
            .collect { language -> language?.let(viewModel::onLanguageSelected) }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<Pair<Int, Int>>(NavigationCoordinator.KEY_REMINDER_TIME)
            .collect { time -> time?.let { (hour, minute) -> viewModel.onReminderTimeSelected(hour, minute) } }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileScreenEffect.NavigateToAuth ->
                    coordinator.navigateAndClearBackStack(CompleteKey)

                ProfileScreenEffect.OpenThemeSheet ->
                    coordinator.showBottomSheet(ThemeSheetKey(viewModel.uiState.value.theme))

                ProfileScreenEffect.OpenLanguageSheet ->
                    coordinator.showBottomSheet(LanguageSheetKey(viewModel.uiState.value.language))

                ProfileScreenEffect.OpenReminderSheet ->
                    coordinator.showBottomSheet(
                        ReminderTimeSheetKey(
                            hour = viewModel.uiState.value.reminderHour,
                            minute = viewModel.uiState.value.reminderMinute,
                        ),
                    )

                ProfileScreenEffect.OpenWidgetSettings ->
                    coordinator.navigate(WidgetSettingsKey)

                ProfileScreenEffect.OpenPaywall -> coordinator.navigate(PaywallKey)

                ProfileScreenEffect.OpenSubscription -> coordinator.navigate(SubscriptionKey)
            }
        }
    }

    ProfileScreen(
        state = state.value,
        intent = viewModel,
        navAction = navAction,
        paddingValues = paddingValues,
    )
}
