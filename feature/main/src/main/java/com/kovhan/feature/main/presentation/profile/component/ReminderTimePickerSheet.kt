package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.design.systems.JetBrainsMonoFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val MINUTE_STEP = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReminderTimePickerSheet(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    val hours = remember { (0..23).toList() }
    val minutes = remember { (0..59 step MINUTE_STEP).toList() }

    val initialHourIndex = remember { hours.indexOf(initialHour.coerceIn(0, 23)).coerceAtLeast(0) }
    val initialMinuteIndex = remember {
        val nearest = minutes.minByOrNull { kotlin.math.abs(it - initialMinute) } ?: 0
        minutes.indexOf(nearest).coerceAtLeast(0)
    }

    var selectedHour by remember { mutableIntStateOf(hours[initialHourIndex]) }
    var selectedMinute by remember { mutableIntStateOf(minutes[initialMinuteIndex]) }

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(R.string.profile_time_picker_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = QuotifyMaterialTheme.dimensions.space6,
                    vertical = QuotifyMaterialTheme.dimensions.space4,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(QuotifyMaterialTheme.dimensions.space4),
            ) {
                WheelPicker(
                    items = hours,
                    initialIndex = initialHourIndex,
                    onSelected = { selectedHour = hours[it] },
                    modifier = Modifier.weight(1f),
                )
                WheelPicker(
                    items = minutes,
                    initialIndex = initialMinuteIndex,
                    onSelected = { selectedMinute = minutes[it] },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(QuotifyMaterialTheme.dimensions.space5))

            PrimaryActionButton(
                text = stringResource(R.string.profile_time_picker_select),
                enabled = true,
                onClick = { onConfirm(selectedHour, selectedMinute) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private val WheelHeight = 180.dp
private val WheelItemHeight = 44.dp

@Composable
private fun WheelPicker(
    items: List<Int>,
    initialIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
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
                .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd))
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
                        text = "%02d".format(value),
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
                    Brush.verticalGradient(listOf(colors.bgElevated, colors.bgElevated.copy(alpha = 0f))),
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height((WheelHeight - WheelItemHeight) / 2)
                .background(
                    Brush.verticalGradient(listOf(colors.bgElevated.copy(alpha = 0f), colors.bgElevated)),
                ),
        )
    }
}
