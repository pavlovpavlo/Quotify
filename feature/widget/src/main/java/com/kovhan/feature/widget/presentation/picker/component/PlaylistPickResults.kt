package com.kovhan.feature.widget.presentation.picker.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.core.models.widget.PlaylistSourceType
import com.kovhan.core.ui.component.emptystate.DefaultEmptyState
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.picker.mvi.PickerScope

@Composable
internal fun PlaylistPickResults(
    scope: PickerScope,
    query: String,
    results: LibrarySearchResults,
    picked: Set<PlaylistSource>,
    onToggle: (PlaylistSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    fun isPicked(type: PlaylistSourceType, id: String) = PlaylistSource(type, id) in picked

    Box(modifier = modifier) {
        when (scope) {
            PickerScope.QUOTES -> {
                if (results.quotes.isEmpty()) {
                    DefaultEmptyState()
                } else {
                    ScopeColumn(results.quotes, { it.id }) { quote ->
                        PickableQuoteCard(
                            quote = quote,
                            query = query,
                            picked = isPicked(PlaylistSourceType.QUOTE, quote.id),
                            onToggle = {
                                onToggle(PlaylistSource(PlaylistSourceType.QUOTE, quote.id))
                            },
                        )
                    }
                }
            }

            PickerScope.FOLDERS -> ScopeColumnOrEmpty(results.folders, { it.id }) { folder ->
                PickableEntityRow(
                    iconRes = CollectionIconMapper.toDrawableRes(folder.iconId),
                    toneKey = folder.iconColor,
                    round = false,
                    name = folder.name,
                    count = folder.quoteCount ?: 0,
                    picked = isPicked(PlaylistSourceType.FOLDER, folder.id),
                    onToggle = { onToggle(PlaylistSource(PlaylistSourceType.FOLDER, folder.id)) },
                )
            }

            PickerScope.BOOKS -> ScopeColumnOrEmpty(results.books, { it.id }) { book ->
                PickableEntityRow(
                    iconRes = DsR.drawable.ic_collection_book,
                    toneKey = null,
                    round = false,
                    name = book.name,
                    count = book.quoteCount ?: 0,
                    picked = isPicked(PlaylistSourceType.BOOK, book.id),
                    onToggle = { onToggle(PlaylistSource(PlaylistSourceType.BOOK, book.id)) },
                )
            }

            PickerScope.AUTHORS -> ScopeColumnOrEmpty(results.authors, { it.id }) { author ->
                PickableEntityRow(
                    iconRes = DsR.drawable.ic_user,
                    toneKey = null,
                    round = true,
                    name = author.name,
                    count = author.quoteCount ?: 0,
                    picked = isPicked(PlaylistSourceType.AUTHOR, author.id),
                    onToggle = { onToggle(PlaylistSource(PlaylistSourceType.AUTHOR, author.id)) },
                )
            }

            PickerScope.TAGS -> PickableTagFlow(
                tags = results.tags,
                isPicked = { isPicked(PlaylistSourceType.TAG, it) },
                onToggle = { onToggle(PlaylistSource(PlaylistSourceType.TAG, it.id)) },
            )
        }
    }
}

@Composable
private fun <T> ScopeColumnOrEmpty(
    items: List<T>,
    key: (T) -> String,
    row: @Composable (T) -> Unit,
) {
    if (items.isEmpty()) {
        DefaultEmptyState()
    } else {
        ScopeColumn(items, key, row)
    }
}

@Composable
private fun <T> ScopeColumn(
    items: List<T>,
    key: (T) -> String,
    row: @Composable (T) -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = dimensions.size18,
            end = dimensions.size18,
            top = dimensions.size16,
            bottom = dimensions.size28,
        ),
        verticalArrangement = Arrangement.spacedBy(dimensions.size12),
    ) {
        items(items, key = key) { row(it) }
    }
}
