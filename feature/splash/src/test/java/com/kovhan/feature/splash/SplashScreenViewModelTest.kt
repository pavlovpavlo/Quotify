package com.kovhan.feature.splash

import app.cash.turbine.test
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.domain.daily.use_case.PrefetchDailyQuotesUseCase
import com.kovhan.domain.library.use_case.sync.RefreshLibraryUseCase
import com.kovhan.domain.library.use_case.sync.SyncPendingChangesUseCase
import com.kovhan.domain.onboarding.use_case.GetOnboardingCompletedUseCase
import com.kovhan.feature.splash.presentation.splash.SplashScreenViewModel
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SplashScreenViewModel startup gate")
class SplashScreenViewModelTest {

    private val getOnboardingCompleted: GetOnboardingCompletedUseCase = mockk(relaxed = true)
    private val isLoggedIn: IsLoggedInUseCase = mockk(relaxed = true)
    private val prefetchDailyQuotes: PrefetchDailyQuotesUseCase = mockk(relaxed = true)
    private val checkConnectivity: CheckConnectivityUseCase = mockk()
    private val subscriptionRepository: SubscriptionRepository = mockk()
    private val syncPendingChanges: SyncPendingChangesUseCase = mockk(relaxed = true)
    private val refreshLibrary: RefreshLibraryUseCase = mockk(relaxed = true)

    private fun viewModel() = SplashScreenViewModel(
        getOnboardingCompleted,
        isLoggedIn,
        prefetchDailyQuotes,
        checkConnectivity,
        subscriptionRepository,
        syncPendingChanges,
        refreshLibrary,
    )

    @Test
    @DisplayName("offline with no subscription shows the blocking dialog")
    fun offlineNoSubscription() = runBlocking {
        coEvery { checkConnectivity() } returns false
        coEvery { subscriptionRepository.isSubscribed() } returns false

        viewModel().uiEffect.test {
            assertEquals(SplashScreenEffect.ShowOfflineBlock, awaitItem())
        }
    }

    @Test
    @DisplayName("online startup syncs the queue and refreshes the library")
    fun onlineSyncsAndRefreshes() = runBlocking {
        coEvery { checkConnectivity() } returns true
        coEvery { syncPendingChanges() } returns true

        viewModel()

        coVerify(timeout = 3_000) { syncPendingChanges() }
        coVerify(timeout = 3_000) { refreshLibrary() }
    }

    @Test
    @DisplayName("offline with a subscription checks the cached status and skips syncing")
    fun offlineSubscribed() = runBlocking {
        coEvery { checkConnectivity() } returns false
        coEvery { subscriptionRepository.isSubscribed() } returns true

        viewModel()

        coVerify(timeout = 3_000) { subscriptionRepository.isSubscribed() }
        coVerify(exactly = 0) { syncPendingChanges() }
    }
}
