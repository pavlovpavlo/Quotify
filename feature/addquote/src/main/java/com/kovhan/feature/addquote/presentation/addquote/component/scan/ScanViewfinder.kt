package com.kovhan.feature.addquote.presentation.addquote.component.scan

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import timber.log.Timber

private val FrameDark = Color(0xFF2B2622)
private val VignetteEdge = Color(0x57281E12)


@Composable
internal fun ScanStage(
    hasCameraPermission: Boolean,
    imageCapture: ImageCapture,
    scanning: Boolean,
    noTextFound: Boolean,
    offline: Boolean,
    aiDenial: com.kovhan.domain.ai.AiDenialReason?,
    onRequestPermission: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
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
                .background(FrameDark),
            contentAlignment = Alignment.Center,
        ) {
            if (offline) {
                ScanOfflinePrompt(onRetry = onRetry)
            } else if (aiDenial != null) {
                ScanAiGatePrompt(reason = aiDenial)
            } else if (hasCameraPermission) {
                CameraPreview(imageCapture = imageCapture, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxSize().vignette())
                CornerMarkers(color = colors.accentAi, modifier = Modifier.fillMaxSize())
            } else {
                PermissionPrompt(onRequestPermission = onRequestPermission)
            }

            if (scanning) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xAA221D18)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensions.iconXl),
                        color = colors.textOnAccent,
                        strokeWidth = 2.dp,
                    )
                }
            }
        }

        Text(
            text = stringResource(
                when {
                    scanning -> R.string.add_quote_scan_caption_scanning
                    offline -> R.string.add_quote_scan_offline_caption
                    noTextFound -> R.string.add_quote_scan_no_text
                    else -> R.string.add_quote_scan_caption_live
                },
            ),
            style = typography.caption,
            color = if (noTextFound && !scanning && !offline) colors.error else colors.textTertiary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CameraPreview(imageCapture: ImageCapture, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    AndroidView(factory = { previewView }, modifier = modifier)

    DisposableEffect(lifecycleOwner) {
        val future = ProcessCameraProvider.getInstance(context)
        var provider: ProcessCameraProvider? = null
        future.addListener(
            {
                provider = future.get().also { cameraProvider ->
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    runCatching {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture,
                        )
                    }.onFailure { Timber.e(it, "Camera bind failed") }
                }
            },
            ContextCompat.getMainExecutor(context),
        )

        onDispose { provider?.unbindAll() }
    }
}

@Composable
private fun ScanOfflinePrompt(onRetry: () -> Unit) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.space6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.space4),
    ) {
        Image(
            modifier = Modifier.size(dimensions.iconXxl),
            painter = painterResource(R.drawable.ic_camera),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.7f)),
        )
        Text(
            text = stringResource(R.string.add_quote_scan_offline),
            style = QuotifyMaterialTheme.typography.body,
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
        )
        QuotifyButton(
            text = stringResource(R.string.add_quote_scan_offline_action),
            onClick = onRetry,
            variant = QuotifyButtonVariant.Filled,
            accent = QuotifyButtonAccent.Primary,
            size = QuotifyButtonSize.Medium,
        )
    }
}

@Composable
private fun PermissionPrompt(onRequestPermission: () -> Unit) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.space6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.space4),
    ) {
        Image(
            modifier = Modifier.size(dimensions.iconXxl),
            painter = painterResource(R.drawable.ic_camera),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.7f)),
        )
        Text(
            text = stringResource(R.string.add_quote_scan_permission),
            style = QuotifyMaterialTheme.typography.body,
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
        )
        QuotifyButton(
            text = stringResource(R.string.add_quote_scan_permission_action),
            onClick = onRequestPermission,
            variant = QuotifyButtonVariant.Filled,
            accent = QuotifyButtonAccent.Primary,
            size = QuotifyButtonSize.Medium,
        )
    }
}

private fun Modifier.vignette(): Modifier = this.background(
    Brush.radialGradient(
        colorStops = arrayOf(
            0.55f to Color.Transparent,
            1f to VignetteEdge,
        ),
    ),
)

@Composable
private fun CornerMarkers(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.drawWithCache {
            val stroke = 2.5.dp.toPx()
            val length = 26.dp.toPx()
            val inset = 16.dp.toPx()
            val left = inset
            val top = inset
            val right = size.width - inset
            val bottom = size.height - inset

            onDrawWithContent {
                drawContent()
                // top-left
                drawLine(color, Offset(left, top), Offset(left + length, top), stroke, StrokeCap.Round)
                drawLine(color, Offset(left, top), Offset(left, top + length), stroke, StrokeCap.Round)
                // top-right
                drawLine(color, Offset(right, top), Offset(right - length, top), stroke, StrokeCap.Round)
                drawLine(color, Offset(right, top), Offset(right, top + length), stroke, StrokeCap.Round)
                // bottom-left
                drawLine(color, Offset(left, bottom), Offset(left + length, bottom), stroke, StrokeCap.Round)
                drawLine(color, Offset(left, bottom), Offset(left, bottom - length), stroke, StrokeCap.Round)
                // bottom-right
                drawLine(color, Offset(right, bottom), Offset(right - length, bottom), stroke, StrokeCap.Round)
                drawLine(color, Offset(right, bottom), Offset(right, bottom - length), stroke, StrokeCap.Round)
            }
        },
    )
}
