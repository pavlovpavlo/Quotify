package com.kovhan.domain.ai.use_case

import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.domain.ai.AiFeature
import com.kovhan.domain.ai.AiUsage
import com.kovhan.domain.ai.AiUsageRepository
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CheckAiAccessUseCase")
class CheckAiAccessUseCaseTest {

    private val isLoggedIn: IsLoggedInUseCase = mockk()
    private val subscriptionRepository: SubscriptionRepository = mockk()
    private val aiUsageRepository: AiUsageRepository = mockk()
    private val useCase = CheckAiAccessUseCase(isLoggedIn, subscriptionRepository, aiUsageRepository)

    @ParameterizedTest(name = "{0}")
    @MethodSource("cases")
    fun resolvesAccess(
        @Suppress("UNUSED_PARAMETER") name: String,
        loggedIn: Boolean,
        subscribed: Boolean,
        feature: AiFeature,
        todayCount: Int,
        monthCount: Int,
        expected: AiAccess,
    ) = runTest {
        every { isLoggedIn() } returns loggedIn
        coEvery { subscriptionRepository.isSubscribed() } returns subscribed
        coEvery { aiUsageRepository.getUsage() } returns AiUsage(todayCount, monthCount)

        assertEquals(expected, useCase(feature))
    }

    fun cases(): Stream<Arguments> = Stream.of(
        Arguments.of(
            "anonymous is refused outright",
            false, false, AiFeature.SCAN, 0, 0,
            AiAccess.Denied(AiDenialReason.NOT_REGISTERED),
        ),
        Arguments.of(
            "free plan cannot use AI tags at all",
            true, false, AiFeature.TAGS, 0, 0,
            AiAccess.Denied(AiDenialReason.SUBSCRIPTION_REQUIRED),
        ),
        Arguments.of(
            "free plan may scan inside the quota",
            true, false, AiFeature.SCAN, 2, 10,
            AiAccess.Allowed,
        ),
        Arguments.of(
            "free plan hits the daily scan limit at 3",
            true, false, AiFeature.SCAN, 3, 10,
            AiAccess.Denied(AiDenialReason.FREE_DAILY_LIMIT_REACHED),
        ),
        Arguments.of(
            "free plan hits the monthly scan limit at 50",
            true, false, AiFeature.SCAN, 0, 50,
            AiAccess.Denied(AiDenialReason.FREE_MONTHLY_LIMIT_REACHED),
        ),
        Arguments.of(
            "subscriber shares one pool across scan and tags",
            true, true, AiFeature.TAGS, 14, 100,
            AiAccess.Allowed,
        ),
        Arguments.of(
            "subscriber hits the daily limit at 15",
            true, true, AiFeature.SCAN, 15, 100,
            AiAccess.Denied(AiDenialReason.DAILY_LIMIT_REACHED),
        ),
        Arguments.of(
            "subscriber hits the monthly limit at 200",
            true, true, AiFeature.TAGS, 0, 200,
            AiAccess.Denied(AiDenialReason.MONTHLY_LIMIT_REACHED),
        ),
    )
}
