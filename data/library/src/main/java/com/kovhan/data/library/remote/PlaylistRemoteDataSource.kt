package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.data.library.dto.PlaylistDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private fun collection() = firestore.userSubcollection(auth, SUBCOLLECTION_PLAYLISTS)

    suspend fun getAll(): List<PlaylistDto> =
        collection()?.get()?.await()?.toObjects(PlaylistDto::class.java).orEmpty()

    suspend fun getById(id: String): PlaylistDto? =
        collection()?.document(id)?.get()?.await()?.toObject(PlaylistDto::class.java)

    suspend fun deleteById(id: String) {
        collection()?.document(id)?.delete()?.await()
    }

    suspend fun edit(dto: PlaylistDto) {
        collection()?.document(dto.id)?.set(dto, SetOptions.merge())?.await()
    }
}
