package com.kovhan.data.library.repository

import com.kovhan.data.library.remote.AiUsageRemoteDataSource
import com.kovhan.domain.ai.AiUsage
import com.kovhan.domain.ai.AiUsageRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiUsageRepositoryImpl @Inject constructor(
    private val remote: AiUsageRemoteDataSource,
) : AiUsageRepository {

    override suspend fun getUsage(): AiUsage {
        val today = LocalDate.now()
        return remote.getUsage(monthKey(today), dayKey(today))
    }

    override suspend fun recordRequest() {
        val today = LocalDate.now()
        remote.recordRequest(monthKey(today), dayKey(today))
    }

    private fun monthKey(date: LocalDate): String = "%04d-%02d".format(date.year, date.monthValue)

    private fun dayKey(date: LocalDate): String =
        "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
}
