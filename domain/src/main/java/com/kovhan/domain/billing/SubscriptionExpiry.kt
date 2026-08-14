package com.kovhan.domain.billing

import com.kovhan.core.models.billing.SubscriptionStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
internal fun Flow<SubscriptionStatus>.reemitOnExpiry(): Flow<SubscriptionStatus> =
    flatMapLatest { status ->
        flow {
            emit(status)
            val remaining = (status.expiresAt ?: return@flow) - System.currentTimeMillis()
            if (remaining > 0) {
                delay(remaining)
                emit(status)
            }
        }
    }
