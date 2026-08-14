package com.kovhan.domain.premium.use_case

import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.library.QuoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CheckQuoteLimitUseCase")
class CheckQuoteLimitUseCaseTest {

    private val isSubscribed: IsSubscribedUseCase = mockk()
    private val quoteRepository: QuoteRepository = mockk()
    private val useCase = CheckQuoteLimitUseCase(isSubscribed, quoteRepository)

    @ParameterizedTest(name = "free user with {0} quotes -> canSave = {1}")
    @CsvSource("0,true", "9,true", "10,false", "11,false")
    @DisplayName("free plan allows up to 10 quotes")
    fun enforcesFreeLimit(count: Int, expected: Boolean) = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { quoteRepository.count() } returns count

        assertTrue(useCase() == expected)
    }

    @Test
    @DisplayName("subscriber is never limited and the count is not even queried")
    fun subscriberIsUnlimited() = runTest {
        coEvery { isSubscribed() } returns true

        assertTrue(useCase())
    }

    @Test
    @DisplayName("free plan blocks exactly at the limit")
    fun blocksAtLimit() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { quoteRepository.count() } returns 10

        assertFalse(useCase())
    }
}
