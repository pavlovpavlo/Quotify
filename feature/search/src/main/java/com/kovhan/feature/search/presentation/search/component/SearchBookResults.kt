package com.kovhan.feature.search.presentation.search.component

import androidx.compose.runtime.Composable
import com.kovhan.core.models.SavedBook
import com.kovhan.core.navigation.EntityType
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchBookResults(
    books: List<SavedBook>,
    navAction: SearchScreenNavAction,
) {
    SearchEntityList(
        items = books,
        key = { it.id },
    ) { book ->
        SearchEntityRow(
            iconRes = DsR.drawable.ic_collection_book,
            toneKey = null,
            round = false,
            name = book.name,
            count = book.quoteCount ?: 0,
            onClick = { navAction.openEntity(EntityType.BOOK, book.id, book.name) },
        )
    }
}
