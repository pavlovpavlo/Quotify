package com.kovhan.domain.premium.use_case

import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.premium.PaywallPromptRepository
import com.kovhan.domain.premium.PremiumLimits
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/** Стартовий екран підписки показуємо не частіше ніж раз на кілька днів і лише без підписки. */
class ShouldShowStartupPaywallUseCase @Inject constructor(
    private val isSubscribed: IsSubscribedUseCase,
    private val repository: PaywallPromptRepository,
) {
    suspend operator fun invoke(): Boolean {
        if (isSubscribed()) return false

        val lastShownAt = repository.lastShownAt()
        // Перший запуск не рахується: онбординг і так щойно розповів про застосунок.
        if (lastShownAt == 0L) {
            repository.markShown(System.currentTimeMillis())
            return false
        }

        val interval = TimeUnit.DAYS.toMillis(PremiumLimits.STARTUP_PAYWALL_INTERVAL_DAYS)
        return System.currentTimeMillis() - lastShownAt >= interval
    }
}
