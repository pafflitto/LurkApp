package com.example.lurk.screens.feed.postviews

import PostData
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.lurk.extensions.format
import com.example.lurk.screens.feed.postviews.Post.Companion.PostType.*
import kotlin.math.round

open class Post(data: PostData)
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

    fun idForIndex(index: Int) = "$index-$id"

    companion object {
        enum class Voted {
            UpVoted,
            DownVoted,
            NoVote
        }
        private enum class PostType(val postHint: String) {
            IMAGE("image"),
            GIF(""),
            TEXT("");
        }

        private fun getType(data: PostData): PostType? {
            return when {
                data.isSelf -> TEXT
                data.url.contains(".gif") ||
                !data.isVideo && data.postHint.contains("video")-> GIF
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
        fun build(
            data: PostData,
        ): Post {
            return when(getType(data)) {
                IMAGE -> ImagePost(data)
                TEXT -> TextPost(data)
                GIF -> GifPost(data)
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
                selftext = "Example text of post"
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
    data: PostData,
): Post(data = data) {
    val url: String = data.url // URL for post
    var image: Drawable? = null
}

class GifPost(
    data: PostData
): Post(data = data) {
    val url: String = when {
        data.domain.contains("gfycat") -> {
            val regex = Regex("(?<=image=).*(?=-)")
            val imageStr = regex.find(data.secureMediaEmbed.content)
            val url = imageStr?.let { Uri.decode(data.secureMediaEmbed.content.substring(imageStr.range)).toString() } ?: ""
            url.plus("-mobile.mp4")
        }
        data.url.contains("imgur") -> {
            var mp4Url = data.url.substring(0, data.url.lastIndexOf(".")) + ".mp4"
            if (!mp4Url.contains("https")) {
                mp4Url = mp4Url.replace("http", "https")
            }
            mp4Url
        }
        data.preview.images.firstOrNull()?.variants?.mp4?.resolutions?.isNotEmpty() == true -> {
            data.preview.images.first().variants.mp4.resolutions.maxByOrNull { it.width }?.let { mp4Resolution ->
                mp4Resolution.url.replace("amp;", "")
            } ?: ""
        }
        else -> data.url
    }

    val thumbnailUrl = data.preview.images.firstOrNull()?.resolutions?.maxByOrNull { it.width }?.url?.replace("amp;", "")
    var thumbnail: Drawable? = null
}