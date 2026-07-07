package com.kovhan.data.library.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kovhan.data.library.dto.DailyQuoteDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyQuoteRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getAll(): List<DailyQuoteDto> =
        firestore.collection(DAILY_QUOTES_COLLECTION)
            .get()
            .await()
            .toObjects(DailyQuoteDto::class.java)
}
