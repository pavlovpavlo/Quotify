package com.kovhan.feature.subscription.presentation.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.bottomsheet.BottomSheetHeight
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.rememberBoldMarkup
import com.kovhan.design.systems.R as DsR

private val cancelSteps = listOf(
    DsR.string.subscription_cancel_step_1,
    DsR.string.subscription_cancel_step_2,
    DsR.string.subscription_cancel_step_3,
    DsR.string.subscription_cancel_step_4,
    DsR.string.subscription_cancel_step_5,
    DsR.string.subscription_cancel_step_6,
)

/**
 * Скасувати підписку зсередини застосунку неможливо — цим керує Google Play.
 * Тому шит лише пояснює кроки, кнопки скасування тут навмисно немає.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManageSubscriptionSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        height = BottomSheetHeight.WRAP_CONTENT,
        title = stringResource(DsR.string.subscription_manage_sheet_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(bottom = 26.dp)
                .navigationBarsPadding(),
        ) {
            Text(
                text = stringResource(DsR.string.subscription_cancel_title),
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                ),
            )

            VerticalSpacer(dimensions.space3)

            Text(
                text = stringResource(DsR.string.subscription_cancel_intro),
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                ),
            )

            VerticalSpacer(dimensions.space5)

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                cancelSteps.forEachIndexed { index, stepRes ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.width(30.dp),
                            text = "${index + 1}.",
                            color = colors.accentPrimary,
                            style = TextStyle(
                                fontFamily = InterFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W700,
                                lineHeight = 23.sp,
                            ),
                        )
                        Text(
                            text = rememberBoldMarkup(stringResource(stepRes)),
                            color = colors.textPrimary,
                            style = TextStyle(
                                fontFamily = InterFamily,
                                fontSize = 15.sp,
                                lineHeight = 23.sp,
                            ),
                        )
                    }
                }
            }

            VerticalSpacer(dimensions.space5)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensions.radiusMd))
                    .background(colors.bgSecondary)
                    .padding(14.dp),
                text = stringResource(DsR.string.subscription_cancel_note),
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                ),
            )
        }
    }
}
