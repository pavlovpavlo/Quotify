package com.kovhan.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

private const val CROSSFADE_MS = 200

/**
 * Circular profile picture with a built-in default avatar for users who never
 * set one.
 *
 * While a newly assigned [photoUrl] is still loading the previously shown image
 * stays on screen underneath it. Without that the avatar blanks out for the
 * duration of the request — which is exactly what happens right after an upload,
 * when the local `content://` preview is swapped for the remote URL.
 */
@Composable
fun UserAvatarImage(
    photoUrl: String?,
    iconSize: Dp,
    modifier: Modifier = Modifier,
) {
    var lastShown by remember { mutableStateOf<String?>(null) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .crossfade(CROSSFADE_MS)
            .build(),
    )
    val isLoaded = painter.state is AsyncImagePainter.State.Success

    LaunchedEffect(photoUrl, isLoaded) {
        lastShown = when {
            photoUrl == null -> null
            isLoaded -> photoUrl
            else -> lastShown
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val previous = lastShown?.takeIf { it != photoUrl }

        if (!isLoaded) {
            if (previous != null) {
                AsyncImage(
                    model = previous,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            } else {
                DefaultAvatar(iconSize)
            }
        }

        if (photoUrl != null) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
private fun DefaultAvatar(iconSize: Dp) {
    Image(
        modifier = Modifier.size(iconSize),
        painter = painterResource(DsR.drawable.ic_user),
        contentDescription = null,
        colorFilter = ColorFilter.tint(QuotifyMaterialTheme.colors.accentPrimary),
    )
}
