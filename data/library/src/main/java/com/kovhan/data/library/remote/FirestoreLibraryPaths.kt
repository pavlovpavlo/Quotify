package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

internal const val USERS_COLLECTION = "users"
internal const val DAILY_QUOTES_COLLECTION = "daily_quotes"
internal const val SUBCOLLECTION_AUTHORS = "saved_authors"
internal const val SUBCOLLECTION_TAGS = "saved_tags"
internal const val SUBCOLLECTION_BOOKS = "saved_books"
internal const val SUBCOLLECTION_COLLECTIONS = "collections"
internal const val SUBCOLLECTION_QUOTES = "quotes"

internal fun FirebaseFirestore.userSubcollection(
    auth: FirebaseAuth,
    name: String,
): CollectionReference? =
    auth.currentUser?.uid?.let { uid ->
        collection(USERS_COLLECTION).document(uid).collection(name)
    }
