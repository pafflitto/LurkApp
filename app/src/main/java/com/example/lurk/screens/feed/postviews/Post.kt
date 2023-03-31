package com.example.lurk.screens.feed.postviews

import PostData
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
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
        private enum class PostType {
            IMAGE,
            GIF,
            TEXT,
            VIDEO;
        }

        private val PostData.type: PostType? get() = when {
            isSelf -> TEXT
            isVideo || url.contains("youtube") -> VIDEO
            GifPost.GifType.gifType(this) != null -> GIF
            else -> typeMap[postHint]
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
            return when(data.type) {
                IMAGE -> ImagePost(data)
                TEXT -> TextPost(data)
                GIF -> GifPost(data)
                VIDEO -> VideoPost(data)
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
    val image = mutableStateOf<Drawable?>(null)
}

class GifPost(
    data: PostData
): Post(data = data) {
    val type = GifType.gifType(data)

    // TODO Change this based on type
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
        data.secureMedia != null -> {
            data.secureMedia!!.redditVideo.dashUrl
        }
        else -> data.url
    }

    val thumbnailUrl = data.preview.images.firstOrNull()?.resolutions?.maxByOrNull { it.width }?.url?.replace("amp;", "")

    // This gets loaded in our paging source
    var thumbnail: Drawable? = null

    sealed class GifType {
        object VReddit : GifType()
        object Direct : GifType()
        object GfyCat : GifType()
        object Imgur : GifType()
        object Streamable : GifType()

        companion object {
            fun gifType(data: PostData): GifType? {
                val realURL = data.url.lowercase()
                return when {
                    realURL.contains("v.redd.it") -> {
                        VReddit
                    }
                    realURL.contains(".mp4")
                            || realURL.contains(".gif")
                            || realURL.contains("webm")
                            || realURL.contains("redditmedia.com")
                            || realURL.contains("preview.redd.it") -> {
                        Direct
                    }
                    (realURL.contains("gfycat") && !realURL.contains("mp4"))
                            && (realURL.contains("redgifs") && !realURL.contains("mp4")) -> {
                        GfyCat
                    }
                    realURL.contains("imgur.com") -> Imgur
                    realURL.contains("streamable.com") -> Streamable
                    else -> null
                }
            }
        }
    }
}

class VideoPost(
    data: PostData
): Post(data = data) {
    val thumbnailUrl = data.preview.images.firstOrNull()?.resolutions?.maxByOrNull { it.width }?.url?.replace("amp;", "")

    // This gets loaded in our paging source
    var thumbnail: Drawable? = null
}