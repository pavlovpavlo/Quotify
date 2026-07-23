package com.kovhan.feature.search.presentation.search.component

import androidx.compose.runtime.Composable
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.navigation.EntityType
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchAuthorResults(
    authors: List<SavedAuthor>,
    navAction: SearchScreenNavAction,
) {
    SearchEntityList(
        items = authors,
        key = { it.id },
    ) { author ->
        SearchEntityRow(
            iconRes = DsR.drawable.ic_user,
            toneKey = null,
            round = true,
            name = author.name,
            count = author.quoteCount ?: 0,
            onClick = { navAction.openEntity(EntityType.AUTHOR, author.id, author.name) },
        )
    }
}
