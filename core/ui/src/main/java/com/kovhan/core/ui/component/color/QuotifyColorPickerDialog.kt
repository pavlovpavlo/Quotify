package com.kovhan.core.ui.component.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.dialog.QuotifyDialog
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Free colour picker: a saturation/value field over a hue slider. Android has no
 * system colour dialog, so this is the app's own — kept in `core:ui` so any
 * feature needing a custom colour uses the same control.
 */
@Composable
fun QuotifyColorPickerDialog(
    initialColor: Color,
    onConfirm: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val initialHsv = remember(initialColor) { initialColor.toHsv() }
    var hue by remember { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember { mutableFloatStateOf(initialHsv[1]) }
    var value by remember { mutableFloatStateOf(initialHsv[2]) }

    val selected = hsvToColor(hue, saturation, value)

    QuotifyDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.space5),
            verticalArrangement = Arrangement.spacedBy(dimensions.space4),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(DsR.string.color_picker_title),
                style = typography.h4,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            SaturationValueField(
                hue = hue,
                saturation = saturation,
                value = value,
                onChange = { newSaturation, newValue ->
                    saturation = newSaturation
                    value = newValue
                },
            )

            HueSlider(hue = hue, onHueChange = { hue = it })

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.space3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.size40)
                        .clip(RoundedCornerShape(dimensions.radiusFull))
                        .background(selected)
                        .border(
                            dimensions.size1,
                            colors.borderStrong,
                            RoundedCornerShape(dimensions.radiusFull),
                        ),
                )
                Text(
                    text = selected.toHexLabel(),
                    style = typography.bodyStrong,
                    color = colors.textSecondary,
                )
            }

            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(DsR.string.color_picker_confirm),
                onClick = { onConfirm(selected) },
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }
    }
}

@Composable
private fun SaturationValueField(
    hue: Float,
    saturation: Float,
    value: Float,
    onChange: (saturation: Float, value: Float) -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensions.size163)
            .clip(RoundedCornerShape(dimensions.radiusLg))
            .background(Brush.horizontalGradient(listOf(Color.White, hsvToColor(hue, 1f, 1f))))
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
            .pointerInput(Unit) {
                fun report(offset: Offset) = onChange(
                    (offset.x / size.width).coerceIn(0f, 1f),
                    1f - (offset.y / size.height).coerceIn(0f, 1f),
                )
                detectTapGestures { report(it) }
            }
            .pointerInput(Unit) {
                fun report(offset: Offset) = onChange(
                    (offset.x / size.width).coerceIn(0f, 1f),
                    1f - (offset.y / size.height).coerceIn(0f, 1f),
                )
                detectDragGestures { change, _ -> report(change.position) }
            },
    ) {
        Thumb(
            color = hsvToColor(hue, saturation, value),
            fractionX = saturation,
            fractionY = 1f - value,
        )
    }
}

@Composable
private fun HueSlider(
    hue: Float,
    onHueChange: (Float) -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensions.size28)
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .background(Brush.horizontalGradient(hueSpectrum))
            .pointerInput(Unit) {
                detectTapGestures {
                    onHueChange((it.x / size.width).coerceIn(0f, 1f) * HUE_MAX)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    onHueChange((change.position.x / size.width).coerceIn(0f, 1f) * HUE_MAX)
                }
            },
    ) {
        Thumb(
            color = hsvToColor(hue, 1f, 1f),
            fractionX = hue / HUE_MAX,
            fractionY = 0.5f,
        )
    }
}

@Composable
private fun BoxScope.Thumb(
    color: Color,
    fractionX: Float,
    fractionY: Float,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusFull)

    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offsetFraction(fractionX, fractionY)
            .size(dimensions.size20)
            .clip(shape)
            .background(color)
            .border(dimensions.size2, Color.White, shape),
    )
}

private fun Modifier.offsetFraction(fractionX: Float, fractionY: Float) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val x = (constraints.maxWidth * fractionX - placeable.width / 2f).roundToInt()
            val y = (constraints.maxHeight * fractionY - placeable.height / 2f).roundToInt()
            placeable.place(x, y)
        }
    }

private val hueSpectrum = listOf(
    Color(0xFFFF0000),
    Color(0xFFFFFF00),
    Color(0xFF00FF00),
    Color(0xFF00FFFF),
    Color(0xFF0000FF),
    Color(0xFFFF00FF),
    Color(0xFFFF0000),
)

private const val HUE_MAX = 360f

private fun Color.toHsv(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsv)
    return hsv
}

private fun hsvToColor(hue: Float, saturation: Float, value: Float): Color =
    Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value)))

private fun Color.toHexLabel(): String =
    String.format(Locale.US, "#%06X", toArgb() and 0xFFFFFF)
