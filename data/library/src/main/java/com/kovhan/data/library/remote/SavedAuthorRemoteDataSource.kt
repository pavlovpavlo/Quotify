package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import com.kovhan.data.library.dto.SavedAuthorDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedAuthorRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private fun collection() = firestore.userSubcollection(auth, SUBCOLLECTION_AUTHORS)

    suspend fun getAll(): List<SavedAuthorDto> =
        collection()?.get()?.await()?.toObjects(SavedAuthorDto::class.java).orEmpty()

    suspend fun getById(id: String): SavedAuthorDto? =
        collection()?.document(id)?.get()?.await()?.toObject(SavedAuthorDto::class.java)

    fun observeAll(): Flow<List<SavedAuthorDto>> = callbackFlow {
        val ref = collection() ?: run {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObjects(SavedAuthorDto::class.java).orEmpty())
        }
        awaitClose { registration.remove() }
    }

    suspend fun deleteById(id: String) {
        collection()?.document(id)?.delete()?.await()
    }

    suspend fun edit(dto: SavedAuthorDto) {
        collection()?.document(dto.id)?.set(dto, SetOptions.merge())?.await()
    }
}
