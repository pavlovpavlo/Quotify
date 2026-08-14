package com.kovhan.domain.premium.use_case

import com.kovhan.domain.premium.PaywallPromptRepository
import javax.inject.Inject

class MarkStartupPaywallShownUseCase @Inject constructor(
    private val repository: PaywallPromptRepository,
) {
    suspend operator fun invoke() = repository.markShown(System.currentTimeMillis())
}
