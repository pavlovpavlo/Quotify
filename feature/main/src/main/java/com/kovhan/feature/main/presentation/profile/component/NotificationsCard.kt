package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.core.ui.extensions.noRippleClickable
import com.kovhan.design.systems.JetBrainsMonoFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun NotificationsCard(
    showQuoteOfDay: Boolean,
    notificationsEnabled: Boolean,
    reminderTime: String,
    onShowQuoteOfDayToggled: (Boolean) -> Unit,
    onNotificationsToggled: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit,
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
            .padding(horizontal = 16.dp)
            .noRippleClickable{
                onReminderTimeClick()
            },
    ) {
        NotificationRow(
            iconRes = R.drawable.ic_home_line,
            title = stringResource(R.string.profile_notif_quote_title),
            subtitle = stringResource(R.string.profile_notif_quote_subtitle),
        ) {
            QuotifySwitch(checked = showQuoteOfDay, onCheckedChange = onShowQuoteOfDayToggled)
        }

        RowDivider()

        NotificationRow(
            iconRes = R.drawable.ic_bell,
            title = stringResource(R.string.profile_notif_push_title),
            subtitle = stringResource(R.string.profile_notif_push_subtitle),
        ) {
            QuotifySwitch(checked = notificationsEnabled, onCheckedChange = onNotificationsToggled)
        }

        AnimatedVisibility(visible = notificationsEnabled) {
            Column {
                RowDivider()
                NotificationRow(
                    iconRes = R.drawable.ic_clock,
                    title = stringResource(R.string.profile_notif_time_title),
                    subtitle = null,
                ) {
                    TimeChip(time = reminderTime)
                }
            }
        }
    }
}

@Composable
private fun TimeChip(
    time: String,
) {
    val colors = QuotifyMaterialTheme.colors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd))
            .background(colors.bgSecondary)
            .border(1.dp, colors.border, RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd))
            .padding(vertical = 5.dp, horizontal = 10.dp),
    ) {
        Text(
            text = time,
            color = colors.textSecondary,
            style = TextStyle(
                fontFamily = JetBrainsMonoFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.W500,
                fontFeatureSettings = "tnum",
            ),
        )
    }
}

