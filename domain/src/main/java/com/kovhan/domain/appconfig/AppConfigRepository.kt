package com.kovhan.domain.appconfig

interface AppConfigRepository {
    suspend fun isUpdateRequired(): Boolean
}
