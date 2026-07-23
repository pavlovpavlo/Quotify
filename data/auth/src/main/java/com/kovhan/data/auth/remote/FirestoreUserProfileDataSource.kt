package com.kovhan.data.auth.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.data.auth.source.RemoteProfile
import com.kovhan.data.auth.source.RemoteUserProfileSource
import com.kovhan.data.auth.util.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserProfileDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RemoteUserProfileSource {

    override suspend fun fetchProfile(uid: String): RemoteProfile? {
        val snapshot = userDoc(uid).get().await()
        if (!snapshot.exists()) return null
        return RemoteProfile(
            username = snapshot.getString(FIELD_USERNAME),
            photoUrl = snapshot.getString(FIELD_PHOTO_URL),
            photoPublicId = snapshot.getString(FIELD_PHOTO_PUBLIC_ID),
        )
    }

    override suspend fun setUsername(uid: String, username: String) {
        userDoc(uid).set(mapOf(FIELD_USERNAME to username), SetOptions.merge()).await()
    }

    override suspend fun setPhoto(uid: String, url: String?, publicId: String?) {
        userDoc(uid).set(
            mapOf(FIELD_PHOTO_URL to url, FIELD_PHOTO_PUBLIC_ID to publicId),
            SetOptions.merge(),
        ).await()
    }

    override suspend fun deleteProfile(uid: String) {
        userDoc(uid).delete().await()
    }

    private fun userDoc(uid: String) = firestore.collection(COLLECTION_USERS).document(uid)

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_USERNAME = "username"
        const val FIELD_PHOTO_URL = "photoUrl"
        const val FIELD_PHOTO_PUBLIC_ID = "photoPublicId"
    }
}
