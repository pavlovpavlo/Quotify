package com.kovhan.feature.entitydetails.presentation.entity_details.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.menu.ActionMenu
import com.kovhan.core.ui.component.menu.ActionMenuItem
import com.kovhan.core.ui.component.quote.ReadOnlyQuoteCard
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * Interactive quote card: the shared [ReadOnlyQuoteCard] plus a three-dot menu
 * (Edit / Move / Delete) in the trailing slot. Shared by collection and entity
 * details.
 */
@Composable
fun EntityQuoteCard(
    quote: EnrichedQuote,
    railColor: Color,
    menuExpanded: Boolean,
    onToggleMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onEdit: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    ReadOnlyQuoteCard(
        quote = quote,
        modifier = modifier,
        railColor = railColor,
        trailingAction = {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                QuotifyIconButton(
                    onClick = onToggleMenu,
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = dimensions.size30,
                    debounceInterval = 0L,
                ) {
                    CompositionLocalProvider(LocalContentColor provides colors.textTertiary) {
                        Image(
                            modifier = Modifier.size(dimensions.size18),
                            painter = painterResource(DsR.drawable.ic_more_vert),
                            contentDescription = stringResource(DsR.string.collection_details_quote_menu_cd),
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                        )
                    }
                }

                ActionMenu(
                    expanded = menuExpanded,
                    onDismiss = onDismissMenu,
                    topOffset = 36.dp,
                    items = listOf(
                        ActionMenuItem(
                            label = stringResource(DsR.string.collection_details_quote_edit),
                            iconRes = DsR.drawable.ic_pencil,
                            onClick = onEdit,
                        ),
                        ActionMenuItem(
                            label = stringResource(DsR.string.collection_details_quote_move),
                            iconRes = DsR.drawable.ic_folder,
                            onClick = onMove,
                        ),
                        ActionMenuItem(
                            label = stringResource(DsR.string.collection_details_quote_delete),
                            iconRes = DsR.drawable.ic_trash,
                            onClick = onDelete,
                            tint = colors.accentPrimary,
                            dividerBefore = true,
                        ),
                    ),
                )
            }
        },
    )
}
