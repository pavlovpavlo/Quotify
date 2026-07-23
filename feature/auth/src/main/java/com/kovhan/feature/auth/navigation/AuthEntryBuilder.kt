package com.kovhan.feature.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.ForgotPasswordKey
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.RegisterKey
import com.kovhan.feature.auth.presentation.complete.navigation.CompleteEntry
import com.kovhan.feature.auth.presentation.forgot_password.navigation.ForgotPasswordEntry
import com.kovhan.feature.auth.presentation.login.navigation.LoginEntry
import com.kovhan.feature.auth.presentation.register.navigation.RegisterEntry
import javax.inject.Inject

class AuthEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<CompleteKey> { CompleteEntry(coordinator, paddingValues) }
        scope.entry<LoginKey> { key -> LoginEntry(coordinator, paddingValues, key.confirmDelete) }
        scope.entry<RegisterKey> { RegisterEntry(coordinator, paddingValues) }
        scope.entry<ForgotPasswordKey> { ForgotPasswordEntry(coordinator, paddingValues) }
    }
}
