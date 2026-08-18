package com.kovhan.data.library.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailyQuoteEntity
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySeenEntity
import com.kovhan.data.library.local.daily.DailySelectionDao
import com.kovhan.data.library.local.daily.DailySelectionEntity
import com.kovhan.data.library.local.library.CollectionDao
import com.kovhan.data.library.local.library.CollectionEntity
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.local.library.QuoteEntity
import com.kovhan.data.library.local.library.SavedAuthorDao
import com.kovhan.data.library.local.library.SavedAuthorEntity
import com.kovhan.data.library.local.library.SavedBookDao
import com.kovhan.data.library.local.library.SavedBookEntity
import com.kovhan.data.library.local.library.SavedTagDao
import com.kovhan.data.library.local.library.SavedTagEntity
import com.kovhan.data.library.local.library.StringListConverter
import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.library.SubscriptionEntity
import com.kovhan.data.library.local.widget.PlaylistDao
import com.kovhan.data.library.local.widget.PlaylistEntity
import com.kovhan.data.library.local.widget.PlaylistSourceEntity
import com.kovhan.data.library.local.widget.WidgetContentDao
import com.kovhan.data.library.local.widget.WidgetQuoteEntity
import com.kovhan.data.library.local.widget.WidgetSeenEntity
import com.kovhan.data.library.local.widget.WidgetStateEntity

@Database(
    entities = [
        DailyQuoteEntity::class,
        DailySelectionEntity::class,
        DailySeenEntity::class,
        QuoteEntity::class,
        CollectionEntity::class,
        SavedAuthorEntity::class,
        SavedBookEntity::class,
        SavedTagEntity::class,
        PendingOperationEntity::class,
        SubscriptionEntity::class,
        PlaylistEntity::class,
        PlaylistSourceEntity::class,
        WidgetQuoteEntity::class,
        WidgetSeenEntity::class,
        WidgetStateEntity::class,
    ],
    version = 11,
    exportSchema = false,
)
@TypeConverters(StringListConverter::class)
abstract class QuotifyDatabase : RoomDatabase() {
    abstract fun dailyQuoteDao(): DailyQuoteDao
    abstract fun dailySelectionDao(): DailySelectionDao
    abstract fun dailySeenDao(): DailySeenDao
    abstract fun quoteDao(): QuoteDao
    abstract fun collectionDao(): CollectionDao
    abstract fun savedAuthorDao(): SavedAuthorDao
    abstract fun savedBookDao(): SavedBookDao
    abstract fun savedTagDao(): SavedTagDao
    abstract fun pendingOperationDao(): PendingOperationDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun widgetContentDao(): WidgetContentDao
}
