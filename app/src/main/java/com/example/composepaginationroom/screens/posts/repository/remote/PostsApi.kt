package com.example.composepaginationroom.screens.posts.repository.remote

import com.example.composepaginationroom.screens.common.model.Post
import retrofit2.http.GET

interface PostsApi {

    @GET("posts")
    suspend fun getPosts(
        /**
         * Current we don't have paginated api support but if required we can use
         * (@Query("page") page: Int) to get paginated data
         */
    ): List<Post>

}