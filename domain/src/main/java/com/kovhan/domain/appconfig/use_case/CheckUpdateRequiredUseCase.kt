package com.kovhan.domain.appconfig.use_case

import com.kovhan.domain.appconfig.AppConfigRepository
import javax.inject.Inject

class CheckUpdateRequiredUseCase @Inject constructor(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(): Boolean = repository.isUpdateRequired()
}
