package com.kovhan.feature.survey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyKey
import com.kovhan.core.navigation.SurveyPlatePreviewKey
import com.kovhan.feature.survey.presentation.plate.navigation.SurveyPlatePreviewEntry
import com.kovhan.feature.survey.presentation.survey.navigation.SurveyEntry
import javax.inject.Inject

class SurveyEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<SurveyKey> { key ->
            SurveyEntry(
                key = key,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }

        scope.entry<SurveyPlatePreviewKey> {
            SurveyPlatePreviewEntry(
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }
    }
}
