package com.example.lurk.repositories

import retrofit2.HttpException

interface Repo {
    /**
     * Function that wraps the response of an api request with a Kotlin Result. This is used to
     * handle HTTP errors received from the reddit api
     */
    suspend fun <T> apiCall(
        authRepo: RedditAuthRepo,
        call: suspend () -> T
    ): Result<T> {
        var result: Result<T>
        var retry = false
        do {
            result = kotlin.runCatching { call() }
                .onFailure {
                    retry = shouldRetryApiFailure(
                        failure = it,
                        authRepo = authRepo
                    )
                }
                .onSuccess {
                    retry = false
                }
        } while (retry)

        return result
    }

    private suspend fun shouldRetryApiFailure(
        failure: Throwable,
        authRepo: RedditAuthRepo
    ): Boolean {
        return if (failure is HttpException && failure.code() == 401) {
            authRepo.refreshToken()
        } else false
    }
}