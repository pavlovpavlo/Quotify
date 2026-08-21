package com.kovhan.feature.survey.presentation.done.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.mapper.WidgetCoverMapper
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Shows the earned cover itself rather than naming it — the photo is the
 * reward, a title for it would say nothing.
 */
@Composable
internal fun SurveyRewardCard(
    coverId: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusLg)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, colors.border, shape)
            .padding(dimensions.size14),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size14),
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size52)
                .clip(RoundedCornerShape(dimensions.size12)),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(WidgetCoverMapper.imageRes(coverId, blurEnabled = false)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(dimensions.size3)) {
            Text(
                text = title,
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    lineHeight = 19.sp,
                ),
            )
            Text(
                text = subtitle,
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                ),
            )
        }
    }
}
