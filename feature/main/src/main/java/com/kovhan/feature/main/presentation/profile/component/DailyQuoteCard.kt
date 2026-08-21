package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

/**
 * Only the quote-of-the-day switch for now — the push rows from
 * [NotificationsCard] wait until notifications actually ship.
 */
@Composable
internal fun DailyQuoteCard(
    showQuoteOfDay: Boolean,
    onShowQuoteOfDayToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusXl)

    Column(
        modifier = modifier
            .shadow(2.dp, shape)
            .clip(shape)
            .background(colors.bgElevated)
            .border(1.dp, colors.border, shape)
            .padding(horizontal = 16.dp),
    ) {
        NotificationRow(
            iconRes = R.drawable.ic_home_line,
            title = stringResource(R.string.profile_notif_quote_title),
            subtitle = stringResource(R.string.profile_notif_quote_subtitle),
        ) {
            QuotifySwitch(checked = showQuoteOfDay, onCheckedChange = onShowQuoteOfDayToggled)
        }
    }
}
