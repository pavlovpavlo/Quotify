package com.kovhan.data.library.di

import com.kovhan.data.library.repository.CollectionRepositoryImpl
import com.kovhan.data.library.repository.QuoteRepositoryImpl
import com.kovhan.data.library.repository.SavedAuthorRepositoryImpl
import com.kovhan.data.library.repository.SavedBookRepositoryImpl
import com.kovhan.data.library.repository.SavedTagRepositoryImpl
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
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
}
