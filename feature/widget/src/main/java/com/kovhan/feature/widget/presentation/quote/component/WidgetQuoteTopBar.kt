package com.kovhan.feature.widget.presentation.quote.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.menu.ActionMenu
import com.kovhan.core.ui.component.menu.ActionMenuItem
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteIntent

@Composable
internal fun WidgetQuoteTopBar(
    menuVisible: Boolean,
    canShowMenu: Boolean,
    intent: WidgetQuoteIntent,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuotifyIconButton(
            onClick = onBack,
            variant = QuotifyButtonVariant.Ghost,
            accent = QuotifyButtonAccent.Neutral,
            size = dimensions.size40,
            debounceInterval = 0L,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size22),
                painter = painterResource(DsR.drawable.ic_back),
                contentDescription = stringResource(DsR.string.details_back_cd),
                colorFilter = ColorFilter.tint(colors.textPrimary),
            )
        }

        Text(
            modifier = Modifier.weight(1f).padding(horizontal = dimensions.size4),
            text = stringResource(DsR.string.widget_quote_title),
            textAlign = TextAlign.Center,
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
            ),
        )

        if (canShowMenu) {
            Box {
                QuotifyIconButton(
                    onClick = intent::onToggleMenu,
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = dimensions.size40,
                    debounceInterval = 0L,
                ) {
                    Image(
                        modifier = Modifier.size(dimensions.size21),
                        painter = painterResource(DsR.drawable.ic_more_vert),
                        contentDescription = stringResource(DsR.string.entity_menu_cd),
                        colorFilter = ColorFilter.tint(colors.textSecondary),
                    )
                }

                ActionMenu(
                    expanded = menuVisible,
                    onDismiss = intent::onDismissMenu,
                    topOffset = dimensions.size44,
                    items = listOf(
                        ActionMenuItem(
                            label = stringResource(DsR.string.widget_quote_edit),
                            iconRes = DsR.drawable.ic_pencil,
                            onClick = intent::onEditClicked,
                        ),
                        ActionMenuItem(
                            label = stringResource(DsR.string.playlist_menu_delete),
                            iconRes = DsR.drawable.ic_trash,
                            onClick = intent::onDeleteClicked,
                            tint = colors.error,
                            dividerBefore = true,
                        ),
                    ),
                )
            }
        } else {
            androidx.compose.foundation.layout.Spacer(Modifier.size(dimensions.size40))
        }
    }
}
