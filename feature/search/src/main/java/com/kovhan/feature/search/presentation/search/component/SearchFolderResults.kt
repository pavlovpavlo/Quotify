package com.kovhan.feature.search.presentation.search.component

import androidx.compose.runtime.Composable
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchFolderResults(
    folders: List<SavedCollection>,
    navAction: SearchScreenNavAction,
) {
    SearchEntityList(
        items = folders,
        key = { it.id },
    ) { folder ->
        SearchEntityRow(
            iconRes = CollectionIconMapper.toDrawableRes(folder.iconId),
            toneKey = folder.iconColor,
            round = false,
            name = folder.name,
            count = folder.quoteCount ?: 0,
            onClick = { navAction.openFolder(folder.id) },
        )
    }
}
