package com.kovhan.feature.splash.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.splash.navigation.SplashScreenNavAction
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenIntent
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenState

@Composable
fun SplashScreen(
    state: SplashScreenState,
    intent: SplashScreenIntent,
    navAction: SplashScreenNavAction,
    paddingValues: PaddingValues
){
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            QuotifyMaterialTheme.animations.loading
        )
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = paddingValues.calculateBottomPadding())
    ) {

        LottieAnimation(
            composition = preloaderLottieComposition,
            modifier = Modifier,
            iterations = 1,
            isPlaying = true
        )
    }
}