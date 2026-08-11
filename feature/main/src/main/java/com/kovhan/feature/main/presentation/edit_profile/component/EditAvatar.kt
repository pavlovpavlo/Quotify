package com.kovhan.feature.main.presentation.edit_profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.UserAvatarImage
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun EditAvatar(
    photoUrl: String?,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier.size(104.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(colors.bgPrimary)
                .padding(3.dp)
                .clip(CircleShape)
                .background(colors.accentPrimarySoft)
                .border(1.dp, colors.border, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            UserAvatarImage(
                photoUrl = photoUrl,
                iconSize = 48.dp,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (-2).dp, y = (-2).dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(if (pressed) colors.accentPrimaryHover else colors.accentPrimary)
                .border(3.dp, colors.bgPrimary, CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onCameraClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(17.dp),
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = stringResource(R.string.profile_edit_avatar_cd),
                colorFilter = ColorFilter.tint(colors.textOnAccent),
            )
        }
    }
}
