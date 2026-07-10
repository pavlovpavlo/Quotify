package com.kovhan.feature.search.presentation.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.EntityDetailsKey
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SearchKey
import com.kovhan.feature.search.presentation.search.SearchScreen
import com.kovhan.feature.search.presentation.search.SearchViewModel

@Composable
internal fun SearchEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state.value,
        action = viewModel,
        navAction = object : SearchScreenNavAction {
            override fun onClose() {
                coordinator.goBack()
            }

            override fun openFolder(collectionId: String) {
                // Opening a folder closes Search (per spec) — pop it, then push the folder.
                coordinator.navigate(
                    key = EntityDetailsKey(
                        type = EntityType.COLLECTION,
                        entityId = collectionId,
                    ),
                    popUpTo = SearchKey,
                    inclusive = true,
                )
            }

            override fun openEntity(type: EntityType, entityId: String, title: String) {
                coordinator.navigate(EntityDetailsKey(type, entityId, title))
            }
        },
        paddingValues = paddingValues,
    )
}
