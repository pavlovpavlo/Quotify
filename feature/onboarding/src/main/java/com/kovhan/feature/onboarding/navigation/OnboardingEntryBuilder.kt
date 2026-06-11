package com.kovhan.feature.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OnboardingKey
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.OnboardingEntry
import javax.inject.Inject

class OnboardingEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<OnboardingKey> {
            OnboardingEntry(coordinator = coordinator, paddingValues = paddingValues)
        }
    }
}
