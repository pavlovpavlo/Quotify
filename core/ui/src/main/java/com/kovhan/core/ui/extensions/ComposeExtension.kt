package com.kovhan.core.ui.extensions

import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import com.kovhan.design.systems.Colors
import com.kovhan.design.systems.Gradients
import com.kovhan.design.systems.QuotifyMaterialTheme

inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier = { this },
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}

fun Modifier.then(
    condition: Boolean,
    other: Modifier
): Modifier {
    return if (condition) then(other) else this
}

@Composable
fun Modifier.noRippleClickable(
    onClick: () -> Unit
): Modifier {
    return this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun Modifier.debouncedClickable(
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    debounceInterval: Long = 1_500L,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastClickTime
            if (timeDifference < debounceInterval) {
                return@clickable
            }

            lastClickTime = currentTime
            onClick()
        }
    )
}

fun ConstrainScope.centerHorizontallyTo(
    other: ConstrainedLayoutReference,
    margin: Dp = 0.dp,
    @FloatRange(from = 0.0, to = 1.0) bias: Float = 0.5f
) {
    linkTo(
        start = other.start,
        end = other.end,
        startMargin = margin,
        endMargin = margin,
        bias = bias
    )
}

fun ConstrainScope.centerVerticallyTo(
    other: ConstrainedLayoutReference,
    margin: Dp = 0.dp,
    @FloatRange(from = 0.0, to = 1.0) bias: Float = 0.5f
) {
    linkTo(
        top = other.top,
        bottom = other.bottom,
        topMargin = margin,
        bottomMargin = margin,
        bias = bias
    )
}

@Composable
fun LazyListState.OnScrolledToEnd(
    threshold: Int = 0,
    block: () -> Unit
) {
    val isScrolledToEnd = remember {
        derivedStateOf {
            val visibleLastItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalsItems = if (layoutInfo.totalItemsCount > threshold) {
                layoutInfo.totalItemsCount - threshold
            } else {
                layoutInfo.totalItemsCount
            }
            visibleLastItem > totalsItems
        }
    }

    LaunchedEffect(isScrolledToEnd.value) {
        if (isScrolledToEnd.value) {
            block()
        }
    }
}

fun LazyGridState.isScrolledToTheEnd(): Boolean {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    return if (layoutInfo.totalItemsCount == 0) {
        false
    } else {
        val lastVisibleItem = visibleItemsInfo.last()
        val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

        (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                (lastVisibleItem.offset.y + lastVisibleItem.size.height) <= viewportHeight)
    }
}

fun Modifier.skeleton(
    shape: Shape? = null,
    duration: Int = 1500,
    enabled: Boolean = true
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton_offset")
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 1F,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "skeleton_offset"
    )

    if (enabled) {
        background(
            brush = Gradients.getSkeleton(
                colorBackground = Colors.primary.copy(
                    alpha = QuotifyMaterialTheme.alpha.alpha_20
                ),
                colorProgress = Colors.tabBg.copy(
                    alpha = QuotifyMaterialTheme.alpha.alpha_50
                ),
                progress = animatedOffset.value
            ),
            shape = shape ?: RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd)
        )
    } else Modifier
}

fun Brush.Companion.solidColor(color: Color): Brush {
    return linearGradient(listOf(color, color))
}

inline val Dp.toPx: Float
    @Composable get() = with(LocalDensity.current) {
        this@toPx.toPx()
    }