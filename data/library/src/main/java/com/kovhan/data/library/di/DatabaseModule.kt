package com.kovhan.data.library.di

import android.content.Context
import androidx.room.Room
import com.kovhan.data.library.local.MIGRATION_4_5
import com.kovhan.data.library.local.MIGRATION_5_6
import com.kovhan.data.library.local.MIGRATION_6_7
import com.kovhan.data.library.local.QuotifyDatabase
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySelectionDao
import com.kovhan.data.library.local.library.CollectionDao
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.local.library.SavedAuthorDao
import com.kovhan.data.library.local.library.SavedBookDao
import com.kovhan.data.library.local.library.SavedTagDao
import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.widget.PlaylistDao
import com.kovhan.data.library.local.widget.WidgetContentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuotifyDatabase =
        Room.databaseBuilder(context, QuotifyDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
            .build()

    @Provides
    fun provideDailyQuoteDao(database: QuotifyDatabase): DailyQuoteDao = database.dailyQuoteDao()

    @Provides
    fun provideDailySelectionDao(database: QuotifyDatabase): DailySelectionDao =
        database.dailySelectionDao()

    @Provides
    fun provideDailySeenDao(database: QuotifyDatabase): DailySeenDao = database.dailySeenDao()

    @Provides
    fun provideQuoteDao(database: QuotifyDatabase): QuoteDao = database.quoteDao()

    @Provides
    fun provideCollectionDao(database: QuotifyDatabase): CollectionDao = database.collectionDao()

    @Provides
    fun provideSavedAuthorDao(database: QuotifyDatabase): SavedAuthorDao = database.savedAuthorDao()

    @Provides
    fun provideSavedBookDao(database: QuotifyDatabase): SavedBookDao = database.savedBookDao()

    @Provides
    fun provideSavedTagDao(database: QuotifyDatabase): SavedTagDao = database.savedTagDao()

    @Provides
    fun providePendingOperationDao(database: QuotifyDatabase): PendingOperationDao =
        database.pendingOperationDao()

    @Provides
    fun provideSubscriptionDao(database: QuotifyDatabase): SubscriptionDao =
        database.subscriptionDao()

    @Provides
    fun providePlaylistDao(database: QuotifyDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun provideWidgetContentDao(database: QuotifyDatabase): WidgetContentDao =
        database.widgetContentDao()

    private const val DATABASE_NAME = "quotify.db"
}
