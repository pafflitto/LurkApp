package com.example.lurk.api

import com.example.lurk.retrofit_clients.DebugInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ListingClient {
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