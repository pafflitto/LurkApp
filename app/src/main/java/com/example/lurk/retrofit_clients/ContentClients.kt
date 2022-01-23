package com.example.lurk.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ListingClient {
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(RedditApiConstants.BASE_CONTENT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListingApiService::class.java)
    }
}