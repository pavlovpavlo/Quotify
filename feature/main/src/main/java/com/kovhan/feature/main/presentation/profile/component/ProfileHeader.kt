package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import com.kovhan.design.systems.JetBrainsMonoFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun ProfileHeader(
    displayName: String,
    username: String,
    photoUrl: String?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    Row(
        modifier = modifier.padding(top = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clickable(onClick = onEditClick),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(colors.bgPrimary)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(colors.accentPrimarySoft)
                    .border(1.dp, colors.border, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                    )
                } else {
                    Text(
                        text = displayName.take(1).uppercase(),
                        color = colors.accentPrimary,
                        style = TextStyle(
                            fontFamily = NewsreaderFamily,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.W600,
                        ),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(colors.accentPrimary)
                    .border(2.dp, colors.bgPrimary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(13.dp),
                    painter = painterResource(R.drawable.ic_pencil),
                    contentDescription = stringResource(R.string.profile_edit_avatar_cd),
                    colorFilter = ColorFilter.tint(colors.textOnAccent),
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = displayName,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
            if (username.isNotBlank()) {
                Spacer(Modifier.size(3.dp))
                Text(
                    text = username,
                    color = colors.accentPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = JetBrainsMonoFamily,
                        fontSize = 13.sp,
                    ),
                )
            }
        }
    }
}
