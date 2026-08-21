package com.kovhan.feature.widget.presentation.frequency

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.JetBrainsMonoFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WidgetFrequencySheet(
    initialHours: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val sheetState = rememberModalBottomSheetState()

    val hours = remember {
        (WidgetSettings.MIN_FREQUENCY_HOURS..WidgetSettings.MAX_FREQUENCY_HOURS).toList()
    }
    val initialIndex = remember {
        hours.indexOf(
            initialHours.coerceIn(
                WidgetSettings.MIN_FREQUENCY_HOURS,
                WidgetSettings.MAX_FREQUENCY_HOURS,
            ),
        ).coerceAtLeast(0)
    }
    var selectedHours by remember { mutableIntStateOf(hours[initialIndex]) }

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(DsR.string.widget_frequency_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.space6, vertical = dimensions.space4),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensions.space2),
                text = pluralStringResource(DsR.plurals.widget_frequency_column, selectedHours),
                textAlign = TextAlign.Center,
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W700,
                    letterSpacing = 1.1.sp,
                ),
            )

            HourWheel(
                items = hours,
                initialIndex = initialIndex,
                onSelected = { selectedHours = hours[it] },
            )

            Spacer(Modifier.height(dimensions.space5))

            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(DsR.string.widget_frequency_confirm),
                onClick = { onConfirm(selectedHours) },
            )
        }
    }
}

private val WheelHeight = 180.dp
private val WheelItemHeight = 44.dp

@Composable
private fun HourWheel(
    items: List<Int>,
    initialIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val density = LocalDensity.current

    val centerIndex by remember {
        derivedStateOf {
            val itemPx = with(density) { WheelItemHeight.toPx() }
            val extra = if (listState.firstVisibleItemScrollOffset > itemPx / 2) 1 else 0
            (listState.firstVisibleItemIndex + extra).coerceIn(items.indices)
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) onSelected(centerIndex)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(WheelHeight),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(WheelItemHeight)
                .clip(RoundedCornerShape(dimensions.radiusMd))
                .background(colors.accentPrimarySoft),
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = (WheelHeight - WheelItemHeight) / 2),
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(items) { index, value ->
                val selected = index == centerIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(WheelItemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = value.toString(),
                        color = if (selected) colors.accentPrimaryHover else colors.textTertiary,
                        style = TextStyle(
                            fontFamily = JetBrainsMonoFamily,
                            fontSize = if (selected) 21.sp else 18.sp,
                            fontWeight = if (selected) FontWeight.W700 else FontWeight.W400,
                            fontFeatureSettings = "tnum",
                        ),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height((WheelHeight - WheelItemHeight) / 2)
                .background(
                    Brush.verticalGradient(
                        listOf(colors.bgElevated, colors.bgElevated.copy(alpha = 0f)),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height((WheelHeight - WheelItemHeight) / 2)
                .background(
                    Brush.verticalGradient(
                        listOf(colors.bgElevated.copy(alpha = 0f), colors.bgElevated),
                    ),
                ),
        )
    }
}
