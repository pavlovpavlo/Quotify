package com.kovhan.data.survey.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kovhan.core.models.survey.SurveyResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Write-only, like feedback: the answers are queued by the Firestore SDK and
 * flushed when connectivity returns, so nothing is awaited here.
 */
@Singleton
class SurveyResponseRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    fun submit(response: SurveyResponse, appVersion: String) {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection(COLLECTION_RESPONSES).add(
            mapOf(
                FIELD_UID to uid,
                FIELD_SURVEY_ID to response.surveyId,
                FIELD_COMPLETED to response.completed,
                FIELD_LANGUAGE to response.language,
                FIELD_APP_VERSION to appVersion,
                FIELD_ANSWERS to response.answers.map { answer ->
                    mapOf(
                        FIELD_QUESTION_ID to answer.questionId,
                        FIELD_QUESTION_TITLE to answer.questionTitle,
                        FIELD_OPTION_IDS to answer.optionIds,
                        FIELD_OPTION_TEXTS to answer.optionTexts,
                        FIELD_INPUTS to answer.inputs,
                    )
                },
                FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            ),
        )
    }

    private companion object {
        const val COLLECTION_RESPONSES = "survey_responses"
        const val FIELD_UID = "uid"
        const val FIELD_SURVEY_ID = "surveyId"
        const val FIELD_COMPLETED = "completed"
        const val FIELD_LANGUAGE = "language"
        const val FIELD_APP_VERSION = "appVersion"
        const val FIELD_ANSWERS = "answers"
        const val FIELD_QUESTION_ID = "questionId"
        const val FIELD_QUESTION_TITLE = "questionTitle"
        const val FIELD_OPTION_IDS = "optionIds"
        const val FIELD_OPTION_TEXTS = "optionTexts"
        const val FIELD_INPUTS = "inputs"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
