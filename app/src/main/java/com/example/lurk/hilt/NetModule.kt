package com.example.lurk.hilt

import com.example.lurk.data.api.RedditApi
import com.example.lurk.data.api.RedditApiConstants
import com.example.lurk.data.api.RedditAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetModule {

    @Provides
    @Singleton
    @Named("redditAuthRetrofit")
    fun provideRedditAuthRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(RedditApiConstants.BASE_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("redditRetrofit")
    fun provideRedditRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(RedditApiConstants.BASE_CONTENT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRedditAuthApi(@Named("redditAuthRetrofit") retrofit: Retrofit): RedditAuthApi =
        retrofit.create(RedditAuthApi::class.java)

    @Provides
    @Singleton
    fun provideRedditApi(@Named("redditRetrofit") retrofit: Retrofit): RedditApi =
        retrofit.create(RedditApi::class.java)
}