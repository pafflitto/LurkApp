package com.example.lurk.hilt

import android.content.Context
import com.example.lurk.RedditImageLoader
import com.example.lurk.RedditImageLoaderImpl
import com.example.lurk.api.RedditApi
import com.example.lurk.api.RedditAuthApi
import com.example.lurk.datastores.RedditAuthDataStore
import com.example.lurk.datastores.UserPreferencesDataStore
import com.example.lurk.repositories.RedditAuthRepo
import com.example.lurk.repositories.RedditAuthRepoImpl
import com.example.lurk.repositories.RedditRepo
import com.example.lurk.repositories.RedditRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    internal fun provideAuthDataStore(
        @ApplicationContext context: Context
    ): RedditAuthDataStore = RedditAuthDataStore(context)

    @Provides
    @Singleton
    internal fun provideUserPrefDataStore(
        @ApplicationContext context: Context
    ): UserPreferencesDataStore = UserPreferencesDataStore(context)

    @Provides
    @Singleton
    internal fun provideRedditAuthRepo(
        authDataStore: RedditAuthDataStore,
        authApi: RedditAuthApi
    ): RedditAuthRepo = RedditAuthRepoImpl(
        authApi,
        authDataStore
    )

    @Provides
    @Singleton
    internal fun provideRedditRepo(
        redditApi: RedditApi,
        authRepo: RedditAuthRepo,
        userPreferencesDataStore: UserPreferencesDataStore,
        imageLoader: RedditImageLoader
    ): RedditRepo = RedditRepoImpl(
        redditApi,
        authRepo,
        userPreferencesDataStore,
        imageLoader
    )

    @Provides
    @Singleton
    internal fun provideImageLoader(
        @ApplicationContext context: Context
    ): RedditImageLoader = RedditImageLoaderImpl(context)
}