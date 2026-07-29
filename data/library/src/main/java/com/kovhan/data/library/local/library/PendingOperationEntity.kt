package com.kovhan.data.library.local.library

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A pending write that must be mirrored to the backend once the device is online.
 * The primary key is derived from ([entityType], [entityId]) so a `REPLACE` insert
 * coalesces repeated edits of the same entity into a single latest-intent row.
 */
@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey val key: String,
    val entityType: String,
    val entityId: String,
    val opType: String,
    val createdAt: Long,
) {
    companion object {
        fun keyOf(entityType: PendingEntityType, entityId: String): String =
            "${entityType.name}:$entityId"

        fun of(
            entityType: PendingEntityType,
            entityId: String,
            opType: PendingOpType,
            createdAt: Long,
        ): PendingOperationEntity = PendingOperationEntity(
            key = keyOf(entityType, entityId),
            entityType = entityType.name,
            entityId = entityId,
            opType = opType.name,
            createdAt = createdAt,
        )
    }
}

enum class PendingEntityType { QUOTE, COLLECTION, AUTHOR, BOOK, TAG, PLAYLIST }

enum class PendingOpType { UPSERT, DELETE }
