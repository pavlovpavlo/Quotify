package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.data.library.dto.QuoteDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private fun collection() = firestore.userSubcollection(auth, SUBCOLLECTION_QUOTES)

    suspend fun getAll(): List<QuoteDto> =
        collection()?.get()?.await()?.toObjects(QuoteDto::class.java).orEmpty()

    suspend fun getById(id: String): QuoteDto? =
        collection()?.document(id)?.get()?.await()?.toObject(QuoteDto::class.java)

    fun observeAll(): Flow<List<QuoteDto>> = callbackFlow {
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
            trySend(snapshot?.toObjects(QuoteDto::class.java).orEmpty())
        }
        awaitClose { registration.remove() }
    }

    suspend fun deleteById(id: String) {
        collection()?.document(id)?.delete()?.await()
    }

    suspend fun edit(dto: QuoteDto) {
        collection()?.document(dto.id)?.set(dto, SetOptions.merge())?.await()
    }

    suspend fun setInPushPlaylist(id: String, added: Boolean) {
        collection()?.document(id)
            ?.set(mapOf(FIELD_IN_PUSH_PLAYLIST to added), SetOptions.merge())?.await()
    }

    suspend fun setInWidgetPlaylist(id: String, added: Boolean) {
        collection()?.document(id)
            ?.set(mapOf(FIELD_IN_WIDGET_PLAYLIST to added), SetOptions.merge())?.await()
    }

    private companion object {
        const val FIELD_IN_PUSH_PLAYLIST = "inPushPlaylist"
        const val FIELD_IN_WIDGET_PLAYLIST = "inWidgetPlaylist"
    }
}
