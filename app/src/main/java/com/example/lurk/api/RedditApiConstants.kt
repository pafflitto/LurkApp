package com.example.lurk.api

import com.example.lurk.BuildConfig

object RedditApiConstants {
    const val BASE_AUTH_URL = "https://www.reddit.com/api/v1/"
    const val USERLESS_GRANT_TYPE_URL = "https://oauth.reddit.com/grants/installed_client"
    const val USER_GRANT_TYPE = "authorization_code"
    const val REFRESH_GRANT_TYPE = "refresh_token"
    const val BASE_CONTENT_URL = "https://oauth.reddit.com/"
    const val STATE = "LURK_APP_LOGIN"
    const val REDIRECT_URL = "lurk://app"
    private const val USER_AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s&response_type=code&state=%s&redirect_uri=%s&duration=permanent&scope=identity account mysubreddits read report save subscribe vote"
    val uriString = String.format(USER_AUTH_URL, BuildConfig.CLIENT_ID, STATE, REDIRECT_URL)
}