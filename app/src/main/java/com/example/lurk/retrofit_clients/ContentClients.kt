package com.example.lurk.retrofit_clients

import com.example.lurk.api.ListingApiService
import com.example.lurk.api.RedditApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Client used to get content from the Reddit API
 */
object RedditClient {
    val webservice by lazy {
         val client = OkHttpClient.Builder().addInterceptor(DebugInterceptor()).build()
        Retrofit.Builder()
            .baseUrl(RedditApiConstants.BASE_CONTENT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListingApiService::class.java)
    }
}