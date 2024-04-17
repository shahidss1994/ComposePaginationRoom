package com.example.composepaginationroom.screens.posts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.composepaginationroom.screens.common.model.Post
import com.example.composepaginationroom.screens.posts.viewModel.PostsViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun PostScreen(onEvent: (PostsScreenEvent) -> Unit) {
    val postsViewModel = hiltViewModel<PostsViewModel>()

    val posts = postsViewModel.getPosts().collectAsLazyPagingItems(Dispatchers.IO)

    LazyColumn {
        items(count = posts.itemCount,
            key = posts.itemKey { "${it.id}" }
        ) { index ->
            posts[index]?.let {
                Post(post = it, onEvent)
            }
        }
        val loader = posts.loadState.mediator
        item { LoadingState(loadState = loader, posts = posts) }
    }

}

@Composable
private fun Post(post: Post, onEvent: (PostsScreenEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(PostsScreenEvent.PostClick(post))
            }
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 10.dp),
            text = post.title ?: ""
        )
    }
    Divider()
}

@Composable
private fun LoadingState(loadState: LoadStates?, posts: LazyPagingItems<Post>) {
    if (loadState?.refresh == LoadState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Loading..."
            )

            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }

    if (loadState?.append == LoadState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }

    if (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) {
        val isPaginatingError = (loadState.append is LoadState.Error) || posts.itemCount > 1
        val error = if (loadState.append is LoadState.Error)
            (loadState.append as LoadState.Error).error
        else
            (loadState.refresh as LoadState.Error).error

        val modifier = if (isPaginatingError) {
            Modifier.padding(8.dp)
        } else {
            Modifier.fillMaxSize()
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!isPaginatingError) {
                Icon(
                    modifier = Modifier
                        .size(64.dp),
                    imageVector = Icons.Rounded.Warning, contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = error.message ?: error.toString(),
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = {
                    posts.refresh()
                },
                content = {
                    Text(text = "Refresh")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            )
        }
    }
}


sealed class PostsScreenEvent {
    data class PostClick(val post: Post) : PostsScreenEvent()
}