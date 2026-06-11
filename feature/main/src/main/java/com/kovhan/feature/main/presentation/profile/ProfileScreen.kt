package com.kovhan.feature.main.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.util.appVersionName
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.feature.main.presentation.profile.component.LanguageBottomSheet
import com.kovhan.feature.main.presentation.profile.component.NotificationsCard
import com.kovhan.feature.main.presentation.profile.component.PremiumBanner
import com.kovhan.feature.main.presentation.profile.component.ProfileHeader
import com.kovhan.feature.main.presentation.profile.component.ProfileStatsCard
import com.kovhan.feature.main.presentation.profile.component.ReminderTimePickerSheet
import com.kovhan.feature.main.presentation.profile.component.SettingsSection
import com.kovhan.feature.main.presentation.profile.component.ThemeBottomSheet
import com.kovhan.feature.main.presentation.profile.component.WidgetSection
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import com.kovhan.feature.main.presentation.profile.navigation.ProfileScreenNavAction

private const val DOCK_RESERVED_SPACE_DP = 90

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    intent: ProfileScreenIntent,
    navAction: ProfileScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
            return@Box
        }

        val displayName = state.user?.displayName?.takeIf { it.isNotBlank() }
            ?: stringResource(R.string.profile_no_name)
        val email = state.user?.email
        val usernameValue = state.user?.username?.takeIf { it.isNotBlank() }
            ?: email?.substringBefore("@")?.takeIf { it.isNotBlank() }
        val username = usernameValue?.let { "@$it" } ?: ""
        val reminderTime = "%02d:%02d".format(state.reminderHour, state.reminderMinute)
        val appVersion = LocalContext.current.appVersionName()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() + DOCK_RESERVED_SPACE_DP.dp,
                ),
        ) {
            ProfileHeader(
                modifier = Modifier.fillMaxWidth(),
                displayName = displayName,
                username = username,
                photoUrl = state.user?.photoUrl,
                onEditClick = navAction::navigateToEditProfile,
            )

            ProfileStatsCard(
                modifier = Modifier.fillMaxWidth(),
                quotes = state.stats.quotes,
                books = state.stats.books,
                folders = state.stats.folders,
                authors = state.stats.authors,
            )

            Spacer(Modifier.height(14.dp))

            PremiumBanner(
                modifier = Modifier.fillMaxWidth(),
                onUpgradeClick = intent::onUpgradeClicked,
            )

            Spacer(Modifier.height(14.dp))

            WidgetSection(
                modifier = Modifier.fillMaxWidth(),
                onCreateClick = intent::onCreateWidgetClicked,
            )

            Spacer(Modifier.height(14.dp))

            NotificationsCard(
                modifier = Modifier.fillMaxWidth(),
                showQuoteOfDay = state.showQuoteOfDay,
                notificationsEnabled = state.notificationsEnabled,
                reminderTime = reminderTime,
                onShowQuoteOfDayToggled = intent::onShowQuoteOfDayToggled,
                onNotificationsToggled = intent::onNotificationsToggled,
                onReminderTimeClick = intent::onReminderTimeClicked,
            )

            Spacer(Modifier.height(14.dp))

            SettingsSection(
                modifier = Modifier.fillMaxWidth(),
                appearanceMeta = stringResource(themeMetaRes(state.theme)),
                languageMeta = stringResource(languageMetaRes(state.language)),
                aboutMeta = appVersion.takeIf { it.isNotBlank() }?.let { "v$it" }.orEmpty(),
                onFoldersClick = navAction::navigateToSettings,
                onEditProfileClick = navAction::navigateToEditProfile,
                onAppearanceClick = intent::onAppearanceClicked,
                onLanguageClick = intent::onLanguageClicked,
                onSubscriptionClick = intent::onUpgradeClicked,
                onRateClick = intent::onRateClicked,
                onSupportClick = navAction::navigateToSettings,
                onAboutClick = navAction::navigateToAbout,
            )
        }

        if (state.isTimePickerVisible) {
            ReminderTimePickerSheet(
                initialHour = state.reminderHour,
                initialMinute = state.reminderMinute,
                onConfirm = { hour, minute ->
                    intent.onReminderTimeSelected(hour, minute)
                    intent.onTimePickerDismissed()
                },
                onDismiss = intent::onTimePickerDismissed,
            )
        }

        if (state.isThemeSheetVisible) {
            ThemeBottomSheet(
                selected = state.theme,
                onThemeSelected = intent::onThemeSelected,
                onDismiss = intent::onThemeSheetDismissed,
            )
        }

        if (state.isLanguageSheetVisible) {
            LanguageBottomSheet(
                selected = state.language,
                onLanguageSelected = intent::onLanguageSelected,
                onDismiss = intent::onLanguageSheetDismissed,
            )
        }
    }
}

private fun themeMetaRes(theme: AppTheme): Int = when (theme) {
    AppTheme.LIGHT -> R.string.profile_theme_light
    AppTheme.DARK -> R.string.profile_theme_dark
    AppTheme.SYSTEM -> R.string.profile_theme_system
}

private fun languageMetaRes(language: AppLanguage): Int = when (language) {
    AppLanguage.UKRAINIAN -> R.string.profile_language_uk
    AppLanguage.ENGLISH -> R.string.profile_language_en
}
