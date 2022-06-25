package com.example.lurk.retrofit_clients

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.HttpException

/**
 * Function that wraps the response of an api request with a Kotlin Result. This is used to
 * handle HTTP errors received from the reddit api
 */
suspend fun <T: Any> handleRequest(requestFunc: suspend () -> T): Result<T> {
    return try {
        Result.success(requestFunc())
    } catch (he: HttpException) {
        Result.failure(he)
    }
}

/**
 * Debugging Retrofit Client Interceptor to see raw json response from the Reddit api
 */
class DebugInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val response = chain.proceed(request)
        val rawJson = response.body!!.string()

        Log.d("ResponseInterceptor", String.format("raw JSON response is: %s", rawJson))

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder().body(ResponseBody.create(response.body?.contentType(), rawJson)).build()
    }
}