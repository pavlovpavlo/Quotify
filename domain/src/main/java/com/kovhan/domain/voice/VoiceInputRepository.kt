package com.kovhan.domain.voice

import kotlinx.coroutines.flow.Flow


interface VoiceInputRepository {

    fun isAvailable(): Boolean

    fun transcript(): Flow<String>
}
