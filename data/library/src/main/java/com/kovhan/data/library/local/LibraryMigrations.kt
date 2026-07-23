package com.kovhan.data.library.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Introduces the offline-first library tables (quotes, collections, saved_*), the
 * pending-sync queue and the subscription cache. Additive only — the existing daily
 * tables are untouched, so no local data is lost.
 */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `quotes` (" +
                "`id` TEXT NOT NULL, `text` TEXT NOT NULL, `authorId` TEXT, `bookId` TEXT, " +
                "`collectionId` TEXT, `tagIds` TEXT NOT NULL, `inPushPlaylist` INTEGER NOT NULL, " +
                "`inWidgetPlaylist` INTEGER NOT NULL, `sourceDailyId` TEXT, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `collections` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, `iconId` TEXT NOT NULL, " +
                "`iconColor` TEXT NOT NULL, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `saved_authors` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `saved_books` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `saved_tags` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `pending_operations` (" +
                "`key` TEXT NOT NULL, `entityType` TEXT NOT NULL, `entityId` TEXT NOT NULL, " +
                "`opType` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`key`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `subscription_status` (" +
                "`id` INTEGER NOT NULL, `isSubscribed` INTEGER NOT NULL, PRIMARY KEY(`id`))",
        )
    }
}
