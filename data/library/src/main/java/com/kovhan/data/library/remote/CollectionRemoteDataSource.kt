package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.data.library.dto.CollectionDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private fun collection() = firestore.userSubcollection(auth, SUBCOLLECTION_COLLECTIONS)

    suspend fun getAll(): List<CollectionDto> =
        collection()?.get()?.await()?.toObjects(CollectionDto::class.java).orEmpty()

    suspend fun getById(id: String): CollectionDto? =
        collection()?.document(id)?.get()?.await()?.toObject(CollectionDto::class.java)

    fun observeAll(): Flow<List<CollectionDto>> = callbackFlow {
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
            trySend(snapshot?.toObjects(CollectionDto::class.java).orEmpty())
        }
        awaitClose { registration.remove() }
    }

    suspend fun deleteById(id: String) {
        collection()?.document(id)?.delete()?.await()
    }

    suspend fun edit(dto: CollectionDto) {
        collection()?.document(dto.id)?.set(dto, SetOptions.merge())?.await()
    }
}
