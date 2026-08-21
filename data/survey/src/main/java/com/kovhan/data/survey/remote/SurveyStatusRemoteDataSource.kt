package com.kovhan.data.survey.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.core.models.survey.SurveyStatuses
import com.kovhan.data.survey.util.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Survey progress lives on the user document, so finishing a questionnaire on
 * one device stops it from being offered on another.
 */
@Singleton
class SurveyStatusRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    suspend fun fetch(): SurveyStatuses? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = userDoc(uid).get().await()
        val surveys = snapshot.get(FIELD_SURVEYS) as? Map<*, *> ?: return SurveyStatuses()

        return SurveyStatuses(
            completed = surveys.stringSet(FIELD_COMPLETED),
            skipped = surveys.stringSet(FIELD_SKIPPED),
            postponed = surveys.timestampMap(FIELD_POSTPONED),
        )
    }

    fun addCompleted(surveyId: String) {
        write(
            mapOf(
                FIELD_COMPLETED to FieldValue.arrayUnion(surveyId),
                FIELD_POSTPONED to mapOf(surveyId to FieldValue.delete()),
            ),
        )
    }

    fun addSkipped(surveyId: String) {
        write(
            mapOf(
                FIELD_SKIPPED to FieldValue.arrayUnion(surveyId),
                FIELD_POSTPONED to mapOf(surveyId to FieldValue.delete()),
            ),
        )
    }

    fun addPostponed(surveyId: String, timestamp: Long) {
        write(mapOf(FIELD_POSTPONED to mapOf(surveyId to timestamp)))
    }

    fun clear() {
        val uid = auth.currentUser?.uid ?: return
        userDoc(uid).set(mapOf(FIELD_SURVEYS to FieldValue.delete()), SetOptions.merge())
    }

    /**
     * Not awaited on purpose: offline the write only settles once the device is
     * back online, and the reward screen must not wait for that. The Firestore
     * SDK keeps the change locally and flushes it later.
     */
    private fun write(surveys: Map<String, Any>) {
        val uid = auth.currentUser?.uid ?: return
        userDoc(uid).set(mapOf(FIELD_SURVEYS to surveys), SetOptions.merge())
    }

    private fun userDoc(uid: String) = firestore.collection(COLLECTION_USERS).document(uid)

    private fun Map<*, *>.stringSet(key: String): Set<String> =
        (this[key] as? List<*>).orEmpty().filterIsInstance<String>().toSet()

    private fun Map<*, *>.timestampMap(key: String): Map<String, Long> =
        (this[key] as? Map<*, *>).orEmpty()
            .mapNotNull { (id, value) ->
                val surveyId = id as? String ?: return@mapNotNull null
                val timestamp = (value as? Number)?.toLong() ?: return@mapNotNull null
                surveyId to timestamp
            }
            .toMap()

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_SURVEYS = "surveys"
        const val FIELD_COMPLETED = "completed"
        const val FIELD_SKIPPED = "skipped"
        const val FIELD_POSTPONED = "postponed"
    }
}
