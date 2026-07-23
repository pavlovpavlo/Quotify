package com.kovhan.feature.addquote.presentation.addquote.component.scan

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min
import kotlin.math.roundToInt

private val FrameDark = Color(0xFF2B2622)
private val Scrim = Color(0xB3211C18)

@Composable
internal fun ScanCropStage(
    imageUri: Uri,
    onCancel: () -> Unit,
    onConfirm: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val context = LocalContext.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var bitmap by remember(imageUri) { mutableStateOf<Bitmap?>(null) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var crop by remember(imageUri) { mutableStateOf<Rect?>(null) }
    var cropping by remember(imageUri) { mutableStateOf(false) }

    LaunchedEffect(imageUri) {
        bitmap = withContext(Dispatchers.Default) { loadBitmap(context, imageUri) }
    }

    val bmp = bitmap
    val layout = if (bmp != null && boxSize != IntSize.Zero) {
        fitLayout(bmp.width.toFloat(), bmp.height.toFloat(), boxSize.width.toFloat(), boxSize.height.toFloat())
    } else {
        null
    }
    val minSizePx = with(density) { dimensions.size56.toPx() }

    LaunchedEffect(layout) {
        if (layout != null && crop == null) {
            val inset = min(layout.rect.width, layout.rect.height) * 0.08f
            crop = Rect(
                layout.rect.left + inset,
                layout.rect.top + inset,
                layout.rect.right - inset,
                layout.rect.bottom - inset,
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = dimensions.space5, vertical = dimensions.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.size14),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 220.dp)
                    .clip(RoundedCornerShape(dimensions.size18))
                    .background(FrameDark)
                    .onSizeChanged { boxSize = it },
                contentAlignment = Alignment.Center,
            ) {
                if (bmp == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensions.iconXl),
                        color = colors.textOnAccent,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )

                    val c = crop
                    if (c != null && layout != null) {
                        CropOverlay(crop = c, accent = colors.accentAi)

                        // Drag the body to move the crop window.
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .absolute(c.left, c.top)
                                .size(
                                    width = with(density) { c.width.toDp() },
                                    height = with(density) { c.height.toDp() },
                                )
                                .pointerInput(layout) {
                                    detectDragGestures { _, drag -> crop = crop?.moveBy(drag, layout.rect) }
                                },
                        )

                        CropHandle(c.left, c.top, density) { d ->
                            crop?.let { cur -> crop = cur.resize(left = cur.left + d.x, top = cur.top + d.y, bounds = layout.rect, min = minSizePx) }
                        }
                        CropHandle(c.right, c.top, density) { d ->
                            crop?.let { cur -> crop = cur.resize(right = cur.right + d.x, top = cur.top + d.y, bounds = layout.rect, min = minSizePx) }
                        }
                        CropHandle(c.left, c.bottom, density) { d ->
                            crop?.let { cur -> crop = cur.resize(left = cur.left + d.x, bottom = cur.bottom + d.y, bounds = layout.rect, min = minSizePx) }
                        }
                        CropHandle(c.right, c.bottom, density) { d ->
                            crop?.let { cur -> crop = cur.resize(right = cur.right + d.x, bottom = cur.bottom + d.y, bounds = layout.rect, min = minSizePx) }
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.add_quote_scan_crop_caption),
                style = typography.caption,
                color = colors.textTertiary,
                textAlign = TextAlign.Center,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.space6, vertical = dimensions.space5),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QuotifyButton(
                text = stringResource(R.string.add_quote_scan_crop_cancel),
                onClick = onCancel,
                variant = QuotifyButtonVariant.Outlined,
                accent = QuotifyButtonAccent.Neutral,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.buttonHeight),
            )
            Spacer(modifier = Modifier.width(dimensions.space3))
            QuotifyButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.add_quote_scan_crop_confirm),
                onClick = onClick@{
                    val c = crop ?: return@onClick
                    if (bmp == null || layout == null || cropping) return@onClick
                    cropping = true
                    scope.launch {
                        val rect = c.toBitmapRect(layout)
                        val uri = withContext(Dispatchers.Default) { cropToCache(context, bmp, rect) }
                        if (uri != null) onConfirm(uri) else cropping = false
                    }
                },
                enabled = crop != null && bmp != null && !cropping,
                loading = cropping,
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Primary,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.buttonHeight),
            )
        }
    }
}

@Composable
private fun CropOverlay(crop: Rect, accent: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(Scrim, topLeft = Offset(0f, 0f), size = Size(w, crop.top))
        drawRect(Scrim, topLeft = Offset(0f, crop.bottom), size = Size(w, h - crop.bottom))
        drawRect(Scrim, topLeft = Offset(0f, crop.top), size = Size(crop.left, crop.height))
        drawRect(Scrim, topLeft = Offset(crop.right, crop.top), size = Size(w - crop.right, crop.height))

        val hairline = 1.dp.toPx()
        drawRect(
            color = Color.White.copy(alpha = 0.7f),
            topLeft = crop.topLeft,
            size = crop.size,
            style = Stroke(width = hairline),
        )
        val guide = Color.White.copy(alpha = 0.22f)
        for (i in 1..2) {
            val gx = crop.left + crop.width * i / 3f
            val gy = crop.top + crop.height * i / 3f
            drawLine(guide, Offset(gx, crop.top), Offset(gx, crop.bottom), hairline)
            drawLine(guide, Offset(crop.left, gy), Offset(crop.right, gy), hairline)
        }

        val stroke = 3.dp.toPx()
        val len = 20.dp.toPx()
        corner(accent, crop.left, crop.top, len, len, stroke)
        corner(accent, crop.right, crop.top, -len, len, stroke)
        corner(accent, crop.left, crop.bottom, len, -len, stroke)
        corner(accent, crop.right, crop.bottom, -len, -len, stroke)
    }
}

private fun DrawScope.corner(color: Color, x: Float, y: Float, dx: Float, dy: Float, stroke: Float) {
    drawLine(color, Offset(x, y), Offset(x + dx, y), stroke, StrokeCap.Round)
    drawLine(color, Offset(x, y), Offset(x, y + dy), stroke, StrokeCap.Round)
}

@Composable
private fun BoxScope.CropHandle(
    x: Float,
    y: Float,
    density: Density,
    onDrag: (Offset) -> Unit,
) {
    val touch = 44.dp
    val half = with(density) { touch.toPx() / 2f }
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .absolute(x - half, y - half)
            .size(touch)
            .pointerInput(Unit) {
                detectDragGestures { _, drag -> onDrag(drag) }
            },
    )
}

/** Absolute pixel placement within a TopStart-aligned parent slot. */
private fun Modifier.absolute(xPx: Float, yPx: Float): Modifier =
    this.offset { IntOffset(xPx.roundToInt(), yPx.roundToInt()) }

private data class FitLayout(val scale: Float, val rect: Rect)

private fun fitLayout(bw: Float, bh: Float, boxW: Float, boxH: Float): FitLayout {
    val scale = min(boxW / bw, boxH / bh)
    val dispW = bw * scale
    val dispH = bh * scale
    val left = (boxW - dispW) / 2f
    val top = (boxH - dispH) / 2f
    return FitLayout(scale, Rect(left, top, left + dispW, top + dispH))
}

private fun Rect.moveBy(drag: Offset, bounds: Rect): Rect {
    val newLeft = (left + drag.x).coerceIn(bounds.left, bounds.right - width)
    val newTop = (top + drag.y).coerceIn(bounds.top, bounds.bottom - height)
    return Rect(newLeft, newTop, newLeft + width, newTop + height)
}

private fun Rect.resize(
    left: Float = this.left,
    top: Float = this.top,
    right: Float = this.right,
    bottom: Float = this.bottom,
    bounds: Rect,
    min: Float,
): Rect {
    val l = left.coerceIn(bounds.left, right - min)
    val t = top.coerceIn(bounds.top, bottom - min)
    val r = right.coerceIn(l + min, bounds.right)
    val b = bottom.coerceIn(t + min, bounds.bottom)
    return Rect(l, t, r, b)
}

private fun Rect.toBitmapRect(layout: FitLayout): android.graphics.Rect {
    val sx = (left - layout.rect.left) / layout.scale
    val sy = (top - layout.rect.top) / layout.scale
    val sw = width / layout.scale
    val sh = height / layout.scale
    return android.graphics.Rect(
        sx.roundToInt(),
        sy.roundToInt(),
        (sx + sw).roundToInt(),
        (sy + sh).roundToInt(),
    )
}
