package com.kovhan.feature.survey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyExitDialogKey
import com.kovhan.core.navigation.SurveyInviteDialogKey
import com.kovhan.feature.survey.presentation.exit.navigation.SurveyExitDialogEntry
import com.kovhan.feature.survey.presentation.invite.navigation.SurveyInviteDialogEntry
import javax.inject.Inject

class SurveyDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<SurveyInviteDialogKey> { key ->
            SurveyInviteDialogEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<SurveyExitDialogKey> {
            SurveyExitDialogEntry(coordinator = coordinator)
        }
    }
}
