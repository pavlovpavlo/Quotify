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

/**
 * Adds the local-only widget playlist tables (playlists + playlist_sources).
 * Additive only — no existing data is touched.
 */
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `playlists` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `playlist_sources` (" +
                "`playlistId` TEXT NOT NULL, `type` TEXT NOT NULL, `refId` TEXT NOT NULL, " +
                "PRIMARY KEY(`playlistId`, `type`, `refId`), " +
                "FOREIGN KEY(`playlistId`) REFERENCES `playlists`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)",
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_playlist_sources_playlistId` " +
                "ON `playlist_sources` (`playlistId`)",
        )
    }
}

/**
 * Adds the widget-content tables: the resolved quote snapshot, the rotation
 * "seen" set and the single-row current-quote state. Additive only.
 */
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `widget_quotes` (" +
                "`quoteId` TEXT NOT NULL, `position` INTEGER NOT NULL, PRIMARY KEY(`quoteId`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `widget_seen` (" +
                "`quoteId` TEXT NOT NULL, `shownAt` INTEGER NOT NULL, PRIMARY KEY(`quoteId`))",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `widget_state` (" +
                "`id` INTEGER NOT NULL, `currentQuoteId` TEXT, `lastRotatedAt` INTEGER NOT NULL, " +
                "PRIMARY KEY(`id`))",
        )
    }
}

/**
 * Widens the subscription cache from a bare boolean to the full status, so the
 * management UI can show "expires on" and "auto-renewing" while offline. Additive only.
 */
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `subscription_status` ADD COLUMN `status` TEXT")
        db.execSQL("ALTER TABLE `subscription_status` ADD COLUMN `expiresAt` INTEGER")
        db.execSQL(
            "ALTER TABLE `subscription_status` ADD COLUMN `autoRenewing` INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL("ALTER TABLE `subscription_status` ADD COLUMN `productId` TEXT")
    }
}

/**
 * Adds the optional page number a quote was taken from. Nullable, so existing
 * rows keep NULL and simply render without a page.
 */
val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `quotes` ADD COLUMN `page` INTEGER")
    }
}
