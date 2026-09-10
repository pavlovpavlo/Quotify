package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.kovhan.core.ui.component.widget.WidgetPreviewQuote
import com.kovhan.core.ui.component.widget.WidgetShowcasePreview
import com.kovhan.core.ui.widget.rememberHomeWidgetPlaced
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun WidgetSection(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusXl)
    val widgetPlaced = rememberHomeWidgetPlaced()

    Column(modifier = modifier) {
        ProfileSectionTitle(stringResource(R.string.profile_widget_section))

        Row(
            modifier = Modifier
                .shadow(2.dp, shape)
                .clip(shape)
                .background(colors.accentPrimarySoft)
                .border(1.dp, colors.accentPrimary.copy(alpha = 0.3f), shape)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.profile_widget_title),
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                    ),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.profile_widget_subtitle),
                    color = colors.textSecondary,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                    ),
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.accentPrimary)
                        .clickable(onClick = onCreateClick)
                        .padding(vertical = 9.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Image(
                        modifier = Modifier.size(15.dp),
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colors.textOnAccent),
                    )
                    Text(
                        text = stringResource(
                            if (widgetPlaced) {
                                R.string.profile_widget_cta_configure
                            } else {
                                R.string.profile_widget_cta
                            },
                        ),
                        color = colors.textOnAccent,
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                        ),
                    )
                }
            }

            WidgetShowcasePreview(
                modifier = Modifier
                    .shadow(6.dp, RoundedCornerShape(14.dp))
                    .size(84.dp),
                quote = WidgetPreviewQuote(
                    text = stringResource(R.string.widget_appearance_preview_quote),
                    author = stringResource(R.string.widget_appearance_preview_author),
                    book = stringResource(R.string.widget_appearance_preview_book),
                ),
                metaMaxLines = 2,
            )
        }
    }
}

