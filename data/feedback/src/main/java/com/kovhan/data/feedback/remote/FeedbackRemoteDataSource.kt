package com.kovhan.data.feedback.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kovhan.core.models.feedback.Feedback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    fun submit(feedback: Feedback): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        firestore.collection(FEEDBACK_COLLECTION).add(
            mapOf(
                FIELD_UID to uid,
                FIELD_LIKED to feedback.liked,
                FIELD_COMMENT to feedback.comment,
                FIELD_SOURCE to feedback.source.name.lowercase(),
                FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            ),
        )
        return true
    }

    private companion object {
        const val FEEDBACK_COLLECTION = "feedback"
        const val FIELD_UID = "uid"
        const val FIELD_LIKED = "liked"
        const val FIELD_COMMENT = "comment"
        const val FIELD_SOURCE = "source"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
