package com.kovhan.domain.connectivity

interface ConnectivityRepository {
    suspend fun isOnline(): Boolean
}
