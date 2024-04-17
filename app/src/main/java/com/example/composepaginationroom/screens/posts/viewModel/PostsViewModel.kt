package com.example.composepaginationroom.screens.posts.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.composepaginationroom.screens.common.model.Post
import com.example.composepaginationroom.screens.posts.repository.PostsRemoteMediator
import com.example.composepaginationroom.screens.posts.repository.remote.PostsApi
import com.example.composepaginationroom.util.CPRDB
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val postsApi: PostsApi,
    private val cprdb: CPRDB,
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 20,
            initialLoadSize = 20, // How many items you want to load initially
        ),
        pagingSourceFactory = {
            // The pagingSourceFactory lambda should always return a brand new PagingSource
            // when invoked as PagingSource instances are not reusable.
            cprdb.postDao().getPosts()
        },
        remoteMediator = PostsRemoteMediator(
            postsApi,
            cprdb,
        )
    )

    fun getPosts(): Flow<PagingData<Post>> = pager.flow

}