package com.kovhan.feature.main.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import com.kovhan.feature.main.presentation.profile.navigation.ProfileScreenNavAction

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    intent: ProfileScreenIntent,
    navAction: ProfileScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(paddingValues),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 32.dp,
                    bottom = QuotifyMaterialTheme.dimensions.bottomBarHeight,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val user = state.user
            val displayName = user?.displayName?.takeIf { it.isNotBlank() }
                ?: stringResource(R.string.profile_no_name)

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(colors.accentPrimarySoft),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayName.take(1).uppercase(),
                    color = colors.accentPrimary,
                    style = typography.h2,
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = displayName,
                color = colors.textPrimary,
                style = typography.h3,
                textAlign = TextAlign.Center,
            )

            user?.email?.let { email ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = email,
                    color = colors.textSecondary,
                    style = typography.body,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.weight(1f))

            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.profile_sign_out),
                onClick = intent::onSignOutClicked,
                loading = state.isSigningOut,
                variant = QuotifyButtonVariant.Outlined,
                accent = QuotifyButtonAccent.Destructive,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(),
            )
        }
    }
}
