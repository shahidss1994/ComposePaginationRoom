package com.example.composepaginationroom.screens.posts.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.composepaginationroom.screens.common.model.Post
import com.example.composepaginationroom.screens.posts.model.PostRemoteKeys
import com.example.composepaginationroom.screens.posts.repository.remote.PostsApi
import com.example.composepaginationroom.util.CPRDB
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator(private val postApi: PostsApi,
                          private val db: CPRDB,
): RemoteMediator<Int, Post>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (db.postRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    /** LoadType.Append
     * When we need to load data at the end of the currently loaded data set, the load parameter is LoadType.APPEND
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Post>): PostRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { post ->
            db.postRemoteKeysDao().getPostRemoteKeyByPostId(post.id?:0)
        }
    }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load parameter is LoadType.PREPEND
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Post>): PostRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { post ->
            db.postRemoteKeysDao().getPostRemoteKeyByPostId(post.id?:0)
        }
    }

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh() is called;
     * so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null.
     * When PagingDataAdapter.refresh() is called, the anchorPosition is the first visible position in the displayed list, so we will need to load the page that contains that specific item.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Post>): PostRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.postRemoteKeysDao().getPostRemoteKeyByPostId(id)
            }
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                //New Query so clear the DB
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = true /*remoteKeys != null*/)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = true /*remoteKeys != null*/)
            }
        }

        try {
            val posts = postApi.getPosts(/*page*/).filter { it.id != null }
            val endOfPaginationReached = true//posts.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.postRemoteKeysDao().deletePostRemoteKeys()
                    db.postDao().deletePosts()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = posts.mapNotNull { post ->
                    post.id?.let {id ->
                        PostRemoteKeys(
                            postId = id,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = nextKey
                        )
                    }
                }

                db.postRemoteKeysDao().insertAll(remoteKeys)
                db.postDao().insertAll(posts.onEachIndexed { _, movie -> movie.page = page })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }


}