package com.kovhan.feature.auth.presentation.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyTextBtn
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenIntent
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenState
import com.kovhan.feature.auth.presentation.complete.navigation.CompleteScreenNavAction

@Composable
fun CompleteScreen(
    state: CompleteScreenState,
    intent: CompleteScreenIntent,
    navAction: CompleteScreenNavAction,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography
    val dimens = QuotifyMaterialTheme.dimensions

    val pillSizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = 52.dp)

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        Spacer(Modifier.height(44.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = dimens.space6),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(dimens.space1))

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(209.dp),
                painter = painterResource(QuotifyMaterialTheme.images.welcomeIllustration),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.complete_eyebrow),
                style = typography.eyebrow.copy(fontSize = 10.sp),
                color = colors.accentPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(dimens.space2))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.complete_title),
                style = typography.h3.copy(fontSize = 26.sp),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .height(IntrinsicSize.Min),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(colors.accentPrimary),
                )
                Text(
                    modifier = Modifier.padding(start = 14.dp),
                    text = stringResource(R.string.complete_quote),
                    style = typography.lede.copy(fontSize = 16.sp),
                    color = colors.textPrimary,
                    textAlign = TextAlign.Start,
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.complete_author),
                style = typography.eyebrow.copy(fontSize = 10.sp),
                color = colors.textTertiary,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimens.space6, end = dimens.space6, bottom = dimens.space5),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.complete_sign_up),
                onClick = intent::onSignUpClicked,
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Primary,
                enabled = !state.isGuestLoading,
                sizeSpec = pillSizeSpec,
            )
            Spacer(Modifier.height(6.dp))
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.complete_sign_in),
                onClick = intent::onSignInClicked,
                variant = QuotifyButtonVariant.Outlined,
                accent = QuotifyButtonAccent.Primary,
                enabled = !state.isGuestLoading,
                sizeSpec = pillSizeSpec,
            )
            Spacer(Modifier.height(6.dp))
            QuotifyTextBtn(
                text = stringResource(R.string.complete_later),
                onClick = intent::onLaterClicked,
                enabled = !state.isGuestLoading,
                accent = QuotifyButtonAccent.Neutral,
            )
        }
    }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingValues.calculateBottomPadding()),
        )

        if (state.isGuestLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(colors.bgPrimary),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = colors.accentPrimary)
            }
        }
    }
}
