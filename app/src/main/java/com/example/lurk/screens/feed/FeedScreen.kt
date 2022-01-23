package com.example.lurk.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.lurk.screens.feed.*
import com.example.lurk.ui.theme.LurkTheme
import kotlinx.coroutines.flow.Flow




@Composable
fun FeedScreen(
    posts: Flow<PagingData<Post>>
)
{
    val lazyPosts = posts.collectAsLazyPagingItems()
    Posts(
        lazyPosts
    )
}

@Composable
fun Posts(posts: LazyPagingItems<Post>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts) { post ->
            if (post != null) {
                PostView(post = post)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PostView(post: Post)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        elevation = 6.dp,
    ) {
        Column {
            RedditTitle(
                post = post,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Content
            when (post.type) {
                Post.Companion.PostType.IMAGE -> ImagePostView(post = post)
                else -> TextPostView(post = post)
            }

            Footer(post)
        }
    }
}

@ExperimentalCoilApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeedScreenPreviewDark()
{
    LurkTheme(darkTheme = true) {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(5)
                {
                    PostView(post = Post.exampleTextPost)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FeedScreenPreviewLight()
{
    LurkTheme(darkTheme = false) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5)
                {
                    PostView(post = Post.exampleTextPost)
                }
            }
        }
    }
}