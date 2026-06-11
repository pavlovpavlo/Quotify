package com.kovhan.data.auth.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal suspend fun <T> Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            val exception = task.exception
            when {
                exception != null -> cont.resumeWith(Result.failure(exception))
                task.isCanceled -> cont.cancel()
                else ->
                    @Suppress("UNCHECKED_CAST")
                    cont.resume(task.result as T)
            }
        }
    }
