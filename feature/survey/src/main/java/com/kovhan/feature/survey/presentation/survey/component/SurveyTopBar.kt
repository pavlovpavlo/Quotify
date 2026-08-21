package com.kovhan.feature.survey.presentation.survey.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SurveyTopBar(
    counter: String?,
    showBack: Boolean,
    onBack: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.size12,
                end = dimensions.size12,
                top = dimensions.size14,
                bottom = dimensions.size6,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBack) {
            SurveyTopBarIcon(
                iconRes = DsR.drawable.ic_back,
                contentDescription = stringResource(DsR.string.survey_back_cd),
                onClick = onBack,
            )
        } else {
            Spacer(Modifier.size(dimensions.size34))
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (counter != null) {
                Text(
                    text = counter,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.W600,
                        letterSpacing = 0.02.em,
                    ),
                )
            }
        }

        SurveyTopBarIcon(
            iconRes = DsR.drawable.ic_close,
            contentDescription = stringResource(DsR.string.survey_exit_cd),
            onClick = onExit,
        )
    }
}

@Composable
private fun SurveyTopBarIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyIconButton(
        onClick = onClick,
        variant = QuotifyButtonVariant.Ghost,
        accent = QuotifyButtonAccent.Neutral,
        size = dimensions.size34,
        debounceInterval = 0L,
    ) {
        Image(
            modifier = Modifier.size(dimensions.size19),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(colors.textSecondary),
        )
    }
}
