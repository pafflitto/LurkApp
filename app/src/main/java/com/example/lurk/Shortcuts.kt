package com.example.lurk

import com.example.lurk.datastores.RedditAuthDataStoreManager


val authDataStore: RedditAuthDataStoreManager get() = LurkApplication.instance().authPrefManager