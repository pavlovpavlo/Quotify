package com.kovhan.domain.voice.use_case

import com.kovhan.domain.voice.VoiceInputRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVoiceInputUseCase @Inject constructor(
    private val repository: VoiceInputRepository,
) {
    operator fun invoke(): Flow<String> = repository.transcript()
}
