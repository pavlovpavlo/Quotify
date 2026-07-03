package com.kovhan.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

fun <T> DataStore<Preferences>.getOnes(key: Preferences.Key<T>): T? =
    runBlocking { get(key).first() }

fun <T> DataStore<Preferences>.getOnes(key: Preferences.Key<T>, defaultValue: T): T =
    runBlocking { get(key, defaultValue).first() }

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): Flow<T?> =
    data.catch { emitEmptyOrRethrow(it) }.map { it[key] }

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
    data.catch { emitEmptyOrRethrow(it) }.map { it[key] ?: defaultValue }

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { it[key] = value }
}

suspend fun <T> DataStore<Preferences>.delete(key: Preferences.Key<T>) {
    edit { it.remove(key) }
}

private suspend fun kotlinx.coroutines.flow.FlowCollector<Preferences>.emitEmptyOrRethrow(
    cause: Throwable,
) {
    if (cause is IOException) emit(emptyPreferences()) else throw cause
}
