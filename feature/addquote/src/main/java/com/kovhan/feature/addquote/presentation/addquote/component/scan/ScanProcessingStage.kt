package com.kovhan.feature.addquote.presentation.addquote.component.scan

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
internal fun ScanProcessingStage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val context = LocalContext.current

    var bitmap by remember(imageUri) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(imageUri) {
        bitmap = imageUri?.let { withContext(Dispatchers.Default) { loadBitmap(context, it) } }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.space5, vertical = dimensions.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.size14),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScanFrame(modifier = Modifier.weight(1f)) {
            bitmap?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }

            ScanFrameProgress()
        }

        Text(
            text = stringResource(R.string.add_quote_scan_caption_scanning),
            style = typography.caption,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
        )
    }
}
