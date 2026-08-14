package com.kovhan.domain.premium.use_case

import com.kovhan.domain.premium.OfferPromptRepository
import com.kovhan.domain.premium.OfferTrigger
import javax.inject.Inject

class MarkOfferShownUseCase @Inject constructor(
    private val repository: OfferPromptRepository,
) {
    suspend operator fun invoke(trigger: OfferTrigger) = repository.markShown(trigger)
}
