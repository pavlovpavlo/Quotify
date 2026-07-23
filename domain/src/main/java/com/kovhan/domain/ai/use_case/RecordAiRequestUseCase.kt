package com.kovhan.domain.ai.use_case

import com.kovhan.domain.ai.AiUsageRepository
import javax.inject.Inject

class RecordAiRequestUseCase @Inject constructor(
    private val aiUsageRepository: AiUsageRepository,
) {
    suspend operator fun invoke() = aiUsageRepository.recordRequest()
}
