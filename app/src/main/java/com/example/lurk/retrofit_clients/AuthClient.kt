package com.example.lurk.retrofit_clients

import com.example.lurk.api.AuthApiService
import com.example.lurk.api.RedditApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AuthClient {
    val webservice by lazy {
         val client = OkHttpClient.Builder().addInterceptor(DebugInterceptor()).build()
        Retrofit.Builder()
            .baseUrl(RedditApiConstants.BASE_AUTH_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}