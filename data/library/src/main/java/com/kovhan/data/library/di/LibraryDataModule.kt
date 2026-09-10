package com.kovhan.data.library.di

import com.kovhan.data.library.connectivity.AndroidConnectivityRepository
import com.kovhan.data.library.local.RoomTransactionRunner
import com.kovhan.data.library.local.TransactionRunner
import com.kovhan.data.library.repository.AiUsageRepositoryImpl
import com.kovhan.data.library.repository.CollectionRepositoryImpl
import com.kovhan.data.library.repository.DailyQuoteRepositoryImpl
import com.kovhan.data.library.repository.PlaylistRepositoryImpl
import com.kovhan.data.library.repository.QuoteRepositoryImpl
import com.kovhan.data.library.repository.SavedAuthorRepositoryImpl
import com.kovhan.data.library.repository.SavedBookRepositoryImpl
import com.kovhan.data.library.repository.SavedTagRepositoryImpl
import com.kovhan.data.library.merge.GuestLibraryMergerImpl
import com.kovhan.data.library.repository.SubscriptionRepositoryImpl
import com.kovhan.data.library.repository.WidgetContentRepositoryImpl
import com.kovhan.data.library.sync.LibrarySynchronizerImpl
import com.kovhan.data.library.util.UuidIdGenerator
import com.kovhan.domain.ai.AiUsageRepository
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.connectivity.ConnectivityRepository
import com.kovhan.domain.daily.DailyQuoteRepository
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.GuestLibraryMerger
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import com.kovhan.domain.library.sync.LibrarySynchronizer
import com.kovhan.domain.widget.PlaylistRepository
import com.kovhan.domain.widget.WidgetContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LibraryDataModule {
    @Binds
    @Singleton
    abstract fun bindSavedAuthorRepository(impl: SavedAuthorRepositoryImpl): SavedAuthorRepository

    @Binds
    @Singleton
    abstract fun bindSavedTagRepository(impl: SavedTagRepositoryImpl): SavedTagRepository

    @Binds
    @Singleton
    abstract fun bindSavedBookRepository(impl: SavedBookRepositoryImpl): SavedBookRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(impl: CollectionRepositoryImpl): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindDailyQuoteRepository(impl: DailyQuoteRepositoryImpl): DailyQuoteRepository

    @Binds
    @Singleton
    abstract fun bindIdGenerator(impl: UuidIdGenerator): IdGenerator

    @Binds
    @Singleton
    abstract fun bindAiUsageRepository(impl: AiUsageRepositoryImpl): AiUsageRepository

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(impl: SubscriptionRepositoryImpl): SubscriptionRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityRepository(
        impl: AndroidConnectivityRepository,
    ): ConnectivityRepository

    @Binds
    @Singleton
    abstract fun bindLibrarySynchronizer(impl: LibrarySynchronizerImpl): LibrarySynchronizer

    @Binds
    @Singleton
    abstract fun bindTransactionRunner(impl: RoomTransactionRunner): TransactionRunner

    @Binds
    @Singleton
    abstract fun bindGuestLibraryMerger(impl: GuestLibraryMergerImpl): GuestLibraryMerger

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindWidgetContentRepository(
        impl: WidgetContentRepositoryImpl,
    ): WidgetContentRepository
}
