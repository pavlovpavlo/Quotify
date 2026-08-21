package com.kovhan.data.survey.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kovhan.core.datastore.get
import com.kovhan.core.models.survey.SurveyDraft
import com.kovhan.core.models.survey.SurveyStatuses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mirror of the remote statuses plus the in-progress draft. Read first on entry
 * so the invite decision never waits on the network.
 */
@Singleton
class SurveyLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun observeStatuses(): Flow<SurveyStatuses> = combine(
        dataStore.get(COMPLETED_IDS, emptySet()),
        dataStore.get(SKIPPED_IDS, emptySet()),
        dataStore.get(POSTPONED, ""),
    ) { completed, skipped, postponed ->
        SurveyStatuses(
            completed = completed,
            skipped = skipped,
            postponed = postponed.toPostponedMap(),
        )
    }

    suspend fun statuses(): SurveyStatuses = observeStatuses().first()

    suspend fun save(statuses: SurveyStatuses) {
        dataStore.edit { prefs ->
            prefs[COMPLETED_IDS] = statuses.completed
            prefs[SKIPPED_IDS] = statuses.skipped
            prefs[POSTPONED] = json.encodeToString(statuses.postponed)
        }
    }

    suspend fun addCompleted(surveyId: String) = update { statuses ->
        statuses.copy(
            completed = statuses.completed + surveyId,
            postponed = statuses.postponed - surveyId,
        )
    }

    suspend fun addSkipped(surveyId: String) = update { statuses ->
        statuses.copy(
            skipped = statuses.skipped + surveyId,
            postponed = statuses.postponed - surveyId,
        )
    }

    suspend fun addPostponed(surveyId: String, timestamp: Long) = update { statuses ->
        statuses.copy(postponed = statuses.postponed + (surveyId to timestamp))
    }

    suspend fun draft(surveyId: String): SurveyDraft? {
        val stored = dataStore.get(draftKey(surveyId)).first() ?: return null
        val dto = runCatching { json.decodeFromString<SurveyDraftDto>(stored) }.getOrNull()
            ?: return null

        return SurveyDraft(
            surveyId = surveyId,
            stepIndex = dto.stepIndex,
            answers = dto.answers.mapValues { (_, ids) -> ids.toSet() },
            inputs = dto.inputs,
        )
    }

    suspend fun saveDraft(draft: SurveyDraft) {
        val dto = SurveyDraftDto(
            stepIndex = draft.stepIndex,
            answers = draft.answers.mapValues { (_, ids) -> ids.toList() },
            inputs = draft.inputs,
        )
        dataStore.edit { prefs -> prefs[draftKey(draft.surveyId)] = json.encodeToString(dto) }
    }

    suspend fun clearDraft(surveyId: String) {
        dataStore.edit { prefs -> prefs.remove(draftKey(surveyId)) }
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.asMap().keys
                .filter { it.name.startsWith(PREFIX) }
                .forEach { prefs.remove(it) }
        }
    }

    private suspend fun update(transform: (SurveyStatuses) -> SurveyStatuses) =
        save(transform(statuses()))

    private fun String.toPostponedMap(): Map<String, Long> =
        if (isBlank()) {
            emptyMap()
        } else {
            runCatching { json.decodeFromString<Map<String, Long>>(this) }.getOrDefault(emptyMap())
        }

    private fun draftKey(surveyId: String) = stringPreferencesKey("${PREFIX}draft_$surveyId")

    @Serializable
    private data class SurveyDraftDto(
        val stepIndex: Int = 0,
        val answers: Map<String, List<String>> = emptyMap(),
        val inputs: Map<String, Map<String, String>> = emptyMap(),
    )

    private companion object {
        const val PREFIX = "survey_"
        val COMPLETED_IDS = stringSetPreferencesKey("${PREFIX}completed_ids")
        val SKIPPED_IDS = stringSetPreferencesKey("${PREFIX}skipped_ids")
        val POSTPONED = stringPreferencesKey("${PREFIX}postponed")
    }
}
