package com.kovhan.feature.entitydetails.presentation.entity_details.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.menu.ActionMenu
import com.kovhan.core.ui.component.menu.ActionMenuItem
import com.kovhan.core.ui.component.toolbar.QuotifyTopAppBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsMenu

@Composable
internal fun EntityDetailsTopBar(
    title: String,
    showCloseIcon: Boolean,
    menu: EntityDetailsMenu,
    menuExpanded: Boolean,
    onBack: () -> Unit,
    onToggleMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onRename: () -> Unit,
    onEditStyle: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyTopAppBar(
        title = title,
        navigationIcon = {
            QuotifyIconButton(
                onClick = onBack,
                variant = QuotifyButtonVariant.Ghost,
                accent = QuotifyButtonAccent.Neutral,
                size = dimensions.size38,
                debounceInterval = 0L,
            ) {
                CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                    Image(
                        modifier = Modifier.size(dimensions.size21),
                        painter = painterResource(
                            if (showCloseIcon) DsR.drawable.ic_close else DsR.drawable.ic_back,
                        ),
                        contentDescription = stringResource(
                            if (showCloseIcon) {
                                DsR.string.collection_details_close_cd
                            } else {
                                DsR.string.details_back_cd
                            },
                        ),
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                    )
                }
            }
        },
        actions = {
            if (menu.isVisible) {
                Box {
                    QuotifyIconButton(
                        onClick = onToggleMenu,
                        variant = QuotifyButtonVariant.Ghost,
                        accent = QuotifyButtonAccent.Neutral,
                        size = dimensions.size38,
                        debounceInterval = 0L,
                    ) {
                        CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                            Image(
                                modifier = Modifier.size(dimensions.size21),
                                painter = painterResource(DsR.drawable.ic_more_vert),
                                contentDescription = stringResource(DsR.string.entity_menu_cd),
                                colorFilter = ColorFilter.tint(LocalContentColor.current),
                            )
                        }
                    }

                    ActionMenu(
                        expanded = menuExpanded,
                        onDismiss = onDismissMenu,
                        topOffset = 44.dp,
                        items = buildList {
                            if (menu.canRename) {
                                add(
                                    ActionMenuItem(
                                        label = stringResource(DsR.string.collection_details_menu_rename),
                                        iconRes = DsR.drawable.ic_pencil,
                                        onClick = onRename,
                                    ),
                                )
                            }
                            if (menu.canEditStyle) {
                                add(
                                    ActionMenuItem(
                                        label = stringResource(DsR.string.collection_details_menu_edit),
                                        iconRes = DsR.drawable.ic_palette,
                                        onClick = onEditStyle,
                                    ),
                                )
                            }
                            if (menu.canDelete) {
                                add(
                                    ActionMenuItem(
                                        label = stringResource(DsR.string.collection_details_menu_delete),
                                        iconRes = DsR.drawable.ic_trash,
                                        onClick = onDelete,
                                        tint = colors.accentPrimary,
                                        dividerBefore = menu.canRename || menu.canEditStyle,
                                    ),
                                )
                            }
                        },
                    )
                }
            }
        },
    )
}
