package com.kovhan.domain.voice.use_case

import com.kovhan.domain.voice.VoiceInputRepository
import javax.inject.Inject

class IsVoiceInputAvailableUseCase @Inject constructor(
    private val repository: VoiceInputRepository,
) {
    operator fun invoke(): Boolean = repository.isAvailable()
}
