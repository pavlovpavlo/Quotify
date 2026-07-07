package com.kovhan.data.library.di

import android.content.Context
import androidx.room.Room
import com.kovhan.data.library.local.QuotifyDatabase
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySelectionDao
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
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideDailyQuoteDao(database: QuotifyDatabase): DailyQuoteDao = database.dailyQuoteDao()

    @Provides
    fun provideDailySelectionDao(database: QuotifyDatabase): DailySelectionDao =
        database.dailySelectionDao()

    @Provides
    fun provideDailySeenDao(database: QuotifyDatabase): DailySeenDao = database.dailySeenDao()

    private const val DATABASE_NAME = "quotify.db"
}
