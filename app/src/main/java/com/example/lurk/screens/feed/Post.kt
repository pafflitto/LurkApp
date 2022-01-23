package com.example.lurk.screens.feed

import kotlin.math.round

data class Post(
    val title: String,
    val author: String,
    val subreddit: String,
    val type: PostType,
    private val ups: Int,
    private val downs: Int,
    val url: String? // URL for post
)
{
    val votes: String get() {
        val diff = ups - downs
        return if (diff / 1000 != 0) {
            "${diff / 1000}.${round((diff % 1000) / 100.0).toInt()}k"
        }
        else {
            diff.toString()
        }
    }
    companion object {
        enum class PostType(val postHint: String) {
            IMAGE("image"),
            TEXT("");
        }

        private val typeMap = mapOf(
            "image" to PostType.IMAGE,
            "" to PostType.TEXT
        )
        fun typeForPostHint(hint: String): PostType = typeMap[hint] ?: PostType.TEXT

        val exampleTextPost = Post(
            title = "Example of a reddit post's title",
            author = "DaisyDoo",
            subreddit = "LurkApp",
            type = PostType.TEXT,
            ups = 10000,
            downs = 10000,
            url = null
        )
    }
}