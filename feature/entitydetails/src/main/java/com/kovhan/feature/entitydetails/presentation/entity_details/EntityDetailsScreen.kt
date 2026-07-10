package com.kovhan.feature.entitydetails.presentation.entity_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.ui.mapper.collectionColor
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.entitydetails.presentation.entity_details.component.EntityQuoteCard
import com.kovhan.feature.entitydetails.presentation.entity_details.component.EntityDetailsEmptyState
import com.kovhan.feature.entitydetails.presentation.entity_details.component.EntityDetailsSummary
import com.kovhan.feature.entitydetails.presentation.entity_details.component.EntityDetailsTopBar
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsScreenIntent
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsState
import com.kovhan.feature.entitydetails.presentation.entity_details.navigation.EntityDetailsScreenNavAction

@Composable
internal fun EntityDetailsScreen(
    state: EntityDetailsState,
    intent: EntityDetailsScreenIntent,
    navAction: EntityDetailsScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    var menuExpanded by remember { mutableStateOf(false) }
    var quoteMenuExpandedId by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = menuExpanded || quoteMenuExpandedId != null) {
        when {
            quoteMenuExpandedId != null -> quoteMenuExpandedId = null
            menuExpanded -> menuExpanded = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.screenPadding),
        ) {
            EntityDetailsTopBar(
                title = state.displayTitle,
                showCloseIcon = state.type == EntityType.COLLECTION,
                menu = state.menu,
                menuExpanded = menuExpanded,
                onBack = navAction::onBack,
                onToggleMenu = { menuExpanded = !menuExpanded },
                onDismissMenu = { menuExpanded = false },
                onRename = {
                    menuExpanded = false
                    intent.onRenameRequested()
                },
                onEditStyle = {
                    menuExpanded = false
                    intent.onEditStyleRequested()
                },
                onDelete = {
                    menuExpanded = false
                    intent.onDeleteRequested()
                },
            )

            EntityDetailsSummary(
                type = state.type,
                summary = state.summary,
            )

            Spacer(modifier = Modifier.height(dimensions.size12))

            if (state.quotes.isEmpty()) {
                if (!state.isLoading) {
                    EntityDetailsEmptyState(isCollection = state.type == EntityType.COLLECTION)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = dimensions.size24),
                    verticalArrangement = Arrangement.spacedBy(dimensions.size12),
                ) {
                    items(items = state.quotes, key = { it.id }) { quote ->
                        val railColor = when (state.type) {
                            EntityType.COLLECTION -> collectionColor(state.summary.tone)
                            else -> collectionColor(
                                quote.collection?.iconColor
                                    ?: SavedCollection.DEFAULT_ICON_COLOR,
                            )
                        }

                        EntityQuoteCard(
                            quote = quote,
                            railColor = railColor,
                            menuExpanded = quoteMenuExpandedId == quote.id,
                            onToggleMenu = {
                                quoteMenuExpandedId = if (quoteMenuExpandedId == quote.id) null else quote.id
                            },
                            onDismissMenu = { quoteMenuExpandedId = null },
                            onEdit = {
                                quoteMenuExpandedId = null
                                intent.onEditQuoteRequested(quote)
                            },
                            onMove = {
                                quoteMenuExpandedId = null
                                intent.onMoveQuoteRequested(quote.id)
                            },
                            onDelete = {
                                quoteMenuExpandedId = null
                                intent.onRemoveQuoteRequested(quote.id)
                            },
                        )
                    }
                }
            }
        }

        if (state.isLoading && state.quotes.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
        }
    }
}
