package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun SettingsSection(
    appearanceMeta: String,
    languageMeta: String,
    aboutMeta: String,
    subscriptionMeta: String,
    onEditProfileClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onRateClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onFaqClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDevToolsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)

    Column(modifier = modifier) {
        ProfileSectionTitle(stringResource(R.string.profile_settings_section))

        Column(
            modifier = Modifier
                .shadow(2.dp, shape)
                .clip(shape)
                .background(colors.bgElevated)
                .border(1.dp, colors.border, shape),
        ) {
            SettingsRow(
                iconRes = R.drawable.ic_pencil,
                label = stringResource(R.string.profile_settings_edit_profile),
                meta = null,
                onClick = onEditProfileClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_palette,
                label = stringResource(R.string.profile_settings_appearance),
                meta = appearanceMeta,
                onClick = onAppearanceClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_globe,
                label = stringResource(R.string.profile_settings_language),
                meta = languageMeta,
                onClick = onLanguageClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_card,
                label = stringResource(R.string.profile_settings_subscription),
                meta = subscriptionMeta,
                onClick = onSubscriptionClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_star,
                label = stringResource(R.string.profile_settings_rate),
                meta = null,
                onClick = onRateClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_feedback,
                label = stringResource(R.string.profile_settings_feedback),
                meta = null,
                onClick = onFeedbackClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_faq,
                label = stringResource(R.string.profile_settings_faq),
                meta = null,
                onClick = onFaqClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_lifebuoy,
                label = stringResource(R.string.profile_settings_support),
                meta = null,
                onClick = onSupportClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_info,
                label = stringResource(R.string.profile_settings_about),
                meta = aboutMeta,
                onClick = onAboutClick,
                showDivider = true,
            )
            SettingsRow(
                iconRes = R.drawable.ic_settings,
                label = stringResource(R.string.dev_tools_title),
                meta = null,
                onClick = onDevToolsClick,
                showDivider = false,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    iconRes: Int,
    label: String,
    meta: String?,
    onClick: () -> Unit,
    showDivider: Boolean,
) {
    val colors = QuotifyMaterialTheme.colors
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 13.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textSecondary),
            )
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                ),
            )
            if (meta != null) {
                Text(
                    text = meta,
                    color = colors.textTertiary,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 12.5.sp,
                    ),
                )
            }
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textTertiary),
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 14.dp, end = 14.dp)
                    .background(colors.borderSubtle),
            )
        }
    }
}
