package com.kovhan.feature.onboarding.presentation.onboarding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingIntent
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.OnboardingScreenNavAction


@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    navAction: OnboardingScreenNavAction,
    paddingValues: PaddingValues,
    viewModel: OnboardingScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

} 