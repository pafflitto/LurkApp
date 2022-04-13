package com.example.lurk.retrofit_clients

import android.util.Log
import com.example.lurk.api.AuthApiService
import com.example.lurk.api.RedditApiConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AuthClient {
    val webservice by lazy {
        // val client = OkHttpClient.Builder().addInterceptor(DebugInterceptor()).build()
        Retrofit.Builder()
            .baseUrl(RedditApiConstants.BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}

class DebugInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val response = chain.proceed(request)
        val rawJson = response.body()!!.string()

        Log.d("ResponseInterceptor", String.format("raw JSON response is: %s", rawJson))

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body(ResponseBody.create(response.body()!!.contentType(), rawJson)).build()
    }
}