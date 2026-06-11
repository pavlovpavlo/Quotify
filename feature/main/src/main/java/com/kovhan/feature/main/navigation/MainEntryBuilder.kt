package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.AboutKey
import com.kovhan.core.navigation.EditProfileKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.FavoritesKey
import com.kovhan.core.navigation.HomeKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.ProfileKey
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.feature.main.presentation.about.navigation.AboutEntry
import com.kovhan.feature.main.presentation.edit_profile.navigation.EditProfileEntry
import com.kovhan.feature.main.presentation.favorites.navigation.FavoritesEntry
import com.kovhan.feature.main.presentation.home.navigation.HomeEntry
import com.kovhan.feature.main.presentation.profile.navigation.ProfileEntry
import com.kovhan.feature.main.presentation.quotes.navigation.QuotesEntry
import javax.inject.Inject

class MainEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<HomeKey> { HomeEntry(coordinator, paddingValues) }
        scope.entry<QuotesKey> { QuotesEntry(coordinator, paddingValues) }
        scope.entry<FavoritesKey> { FavoritesEntry(coordinator, paddingValues) }
        scope.entry<ProfileKey> { ProfileEntry(coordinator, paddingValues) }
        scope.entry<EditProfileKey> { EditProfileEntry(coordinator, paddingValues) }
        scope.entry<AboutKey> { AboutEntry(coordinator, paddingValues) }
    }
}
