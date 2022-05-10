package com.example.lurk.screens.feed

import PostData
import com.example.lurk.format
import com.example.lurk.screens.feed.Post.Companion.PostType.IMAGE
import com.example.lurk.screens.feed.Post.Companion.PostType.TEXT
import kotlin.math.round

open class Post(private val data: PostData)
{
    val id: String = data.id
    val title: String = data.title
    val author: String = data.author
    val subreddit: String = data.subredditNamePrefixed
    val totalComments: Int = data.numComments
    var voted: Voted = Voted.NoVote
    private val ups: Int = data.ups
    private val downs: Int = data.downs
    var clicked: Boolean = data.clicked

    val votes: String get() {
        val diff = ups - downs
        return if (diff / 1000 != 0) {
            "${diff / 1000}.${round((diff % 1000) / 100.0).toInt()}k"
        }
        else {
            diff.toString()
        }
    }

    val commentsString: String get() {
        return if (totalComments / 1000 != 0) {
            (totalComments / 1000 + (totalComments % 1000) / 1000.0).format(1) + "k"
        }
        else {
            totalComments.toString()
        }
    }
    companion object {
        enum class Voted {
            UpVoted,
            DownVoted,
            NoVote
        }
        private enum class PostType(val postHint: String) {
            IMAGE("image"),
            TEXT("");
        }

        private fun getType(data: PostData): PostType? {
            return when {
                data.isSelf -> TEXT
                else -> typeMap[data.postHint]
            }
        }

        private val typeMap = mapOf(
            "image" to IMAGE,
            "" to TEXT
        )

        /**
         * Function that creates an instance of a post with respect to its type
         */
        fun build(data: PostData): Post {
            return when(getType(data)) {
                IMAGE -> ImagePost(data)
                TEXT -> TextPost(data)
                else -> Post(data)
            }
        }

        val exampleTextPost = TextPost(
            PostData(
                title = "Example of a reddit post's title",
                author = "DaisyDoo",
                subreddit = "LurkApp",
                numComments = 587,
                ups = 10000,
                downs = 10000,
                selftext = "Example of a text post. GRACIE IS THE BEST GF OF ALL TIME. SHE MAKES ME LAUGH EVERYDAY. I WANT TO SNOG HER FACE"
            )
        )

        val exampleImagePost = ImagePost(
            PostData(
                title = "Example of a reddit post's title",
                author = "DaisyDoo",
                subreddit = "LurkApp",
                numComments = 587,
                ups = 10000,
                downs = 10000,
                url = ""
            )
        )

    }
}

class TextPost(
    data: PostData,
): Post(data = data) {
    val text = data.selftext
}

class ImagePost(
    data: PostData
): Post(data = data) {
    val url: String = data.url // URL for post
}