package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private val WidgetGradientStart = Color(0xFFC8553D)
private val WidgetGradientEnd = Color(0xFF8C3F2E)

@Composable
internal fun WidgetSection(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusXl)

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
                        text = stringResource(R.string.profile_widget_cta),
                        color = colors.textOnAccent,
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                        ),
                    )
                }
            }

            WidgetPreview()
        }
    }
}

@Composable
private fun WidgetPreview() {
    val colors = QuotifyMaterialTheme.colors
    val outerShape = RoundedCornerShape(16.dp)
    val innerShape = RoundedCornerShape(11.dp)

    Box(
        modifier = Modifier
            .shadow(6.dp, outerShape)
            .size(96.dp)
            .clip(outerShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(WidgetGradientStart, WidgetGradientEnd),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            )
            .padding(7.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(innerShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.10f), Color.Transparent),
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(innerShape)
                .background(colors.bgElevated)
                .padding(9.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.profile_widget_preview_quote),
                color = colors.textPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 9.5.sp,
                    lineHeight = 12.sp,
                ),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(colors.accentPrimary),
                )
                Text(
                    text = stringResource(R.string.profile_widget_preview_brand),
                    color = colors.textTertiary,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.W700,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }
    }
}
