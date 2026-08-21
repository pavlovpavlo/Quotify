package com.kovhan.data.survey.repository

import android.content.Context
import android.content.pm.PackageManager
import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyDraft
import com.kovhan.core.models.survey.SurveyResponse
import com.kovhan.core.models.survey.SurveyStatuses
import com.kovhan.data.survey.local.SurveyLocalDataSource
import com.kovhan.data.survey.remote.SurveyConfigParser
import com.kovhan.data.survey.remote.SurveyConfigRemoteDataSource
import com.kovhan.data.survey.remote.SurveyResponseRemoteDataSource
import com.kovhan.data.survey.remote.SurveyStatusRemoteDataSource
import com.kovhan.domain.survey.SurveyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val config: SurveyConfigRemoteDataSource,
    private val parser: SurveyConfigParser,
    private val status: SurveyStatusRemoteDataSource,
    private val responses: SurveyResponseRemoteDataSource,
    private val local: SurveyLocalDataSource,
) : SurveyRepository {

    override suspend fun surveys(language: String): List<Survey> =
        parser.parse(config.rawConfig(), language).filter { it.enabled }

    override suspend fun survey(surveyId: String, language: String): Survey? =
        parser.parse(config.rawConfig(), language).firstOrNull { it.id == surveyId }

    override suspend fun statuses(): SurveyStatuses {
        val cached = local.statuses()
        // Офлайн get() може висіти скільки завгодно — рішення про запрошення
        // не варте затримки старту, тому падаємо назад на локальний кеш.
        val remote = withTimeoutOrNull(REMOTE_STATUS_TIMEOUT_MS) {
            runCatching { status.fetch() }.getOrNull()
        } ?: return cached

        val merged = cached.merge(remote)
        if (merged != cached) local.save(merged)
        return merged
    }

    override fun observeStatuses(): Flow<SurveyStatuses> = local.observeStatuses()

    override suspend fun markCompleted(surveyId: String) {
        local.addCompleted(surveyId)
        runCatching { status.addCompleted(surveyId) }
    }

    override suspend fun markSkipped(surveyId: String) {
        local.addSkipped(surveyId)
        runCatching { status.addSkipped(surveyId) }
    }

    override suspend fun markPostponed(surveyId: String) {
        val now = System.currentTimeMillis()
        local.addPostponed(surveyId, now)
        runCatching { status.addPostponed(surveyId, now) }
    }

    override suspend fun submitResponse(response: SurveyResponse) {
        responses.submit(response, appVersion())
    }

    override suspend fun saveDraft(draft: SurveyDraft) = local.saveDraft(draft)

    override suspend fun draft(surveyId: String): SurveyDraft? = local.draft(surveyId)

    override suspend fun clearDraft(surveyId: String) = local.clearDraft(surveyId)

    override suspend fun resetState() {
        local.clear()
        runCatching { status.clear() }
    }

    private fun appVersion(): String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

    private companion object {
        const val REMOTE_STATUS_TIMEOUT_MS = 3_000L
    }
}
