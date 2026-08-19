package com.kovhan.data.config.repository

import android.content.Context
import android.content.pm.PackageManager
import com.kovhan.data.config.remote.AppConfigRemoteDataSource
import com.kovhan.domain.appconfig.AppConfigRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: AppConfigRemoteDataSource,
) : AppConfigRepository {

    override suspend fun isUpdateRequired(): Boolean {
        val minVersionCode = runCatching { remote.minSupportedVersionCode() }.getOrDefault(0L)
        if (minVersionCode <= 0L) return false
        val currentVersionCode = currentVersionCode() ?: return false
        return currentVersionCode < minVersionCode
    }

    private fun currentVersionCode(): Long? = try {
        context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}
