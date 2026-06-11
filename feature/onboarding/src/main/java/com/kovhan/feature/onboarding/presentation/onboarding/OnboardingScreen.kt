package com.kovhan.feature.onboarding.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyTextBtn
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.onboarding.presentation.onboarding.component.OnboardingPageIndicator
import com.kovhan.feature.onboarding.presentation.onboarding.component.OnboardingSlide1Content
import com.kovhan.feature.onboarding.presentation.onboarding.component.OnboardingSlide2Content
import com.kovhan.feature.onboarding.presentation.onboarding.component.OnboardingSlide3Content
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenIntent
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenState
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.OnboardingScreenNavAction

@Composable
fun OnboardingScreen(
    state: OnboardingScreenState,
    intent: OnboardingScreenIntent,
    navAction: OnboardingScreenNavAction,
    paddingValues: PaddingValues,
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { OnboardingScreenState.PAGE_COUNT },
    )

    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    val latestStatePage by rememberUpdatedState(state.currentPage)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != latestStatePage) intent.onPageChanged(page)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuotifyMaterialTheme.colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = QuotifyMaterialTheme.dimensions.space5,
                    vertical = QuotifyMaterialTheme.dimensions.space2,
                ),
            contentAlignment = Alignment.CenterEnd,
        ) {
            QuotifyTextBtn(
                text = stringResource(R.string.onboarding_skip),
                onClick = intent::onSkipClicked,
                enabled = !state.isCompleting,
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->
            when (page) {
                0 -> OnboardingSlide1Content()
                1 -> OnboardingSlide2Content()
                2 -> OnboardingSlide3Content()
            }
        }

        OnboardingPageIndicator(
            pageCount = OnboardingScreenState.PAGE_COUNT,
            currentPage = state.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    top = QuotifyMaterialTheme.dimensions.space5,
                    bottom = QuotifyMaterialTheme.dimensions.space2,
                ),
        )

        QuotifyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = QuotifyMaterialTheme.dimensions.space6,
                    vertical = QuotifyMaterialTheme.dimensions.space4,
                ),
            text = if (state.isLastPage) {
                stringResource(R.string.onboarding_done)
            } else {
                stringResource(R.string.onboarding_next)
            },
            enabled = !state.isCompleting,
            loading = state.isCompleting,
            onClick = intent::onNextClicked,
        )
    }
}
