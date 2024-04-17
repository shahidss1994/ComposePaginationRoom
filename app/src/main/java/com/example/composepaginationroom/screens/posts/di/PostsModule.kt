package com.example.composepaginationroom.screens.posts.di

import android.content.Context
import com.example.composepaginationroom.screens.posts.repository.remote.PostsApi
import com.example.composepaginationroom.util.CPRDB
import com.example.composepaginationroom.util.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class PostsModule {

    @Provides
    fun providePostsApi(
        @ApplicationContext context: Context,
        retrofitBuilder: RetrofitBuilder
    ): PostsApi {
        return retrofitBuilder.buildApi(PostsApi::class.java, context)
    }

    @Provides
    fun providePostDao(db: CPRDB) = db.postDao()

    @Provides
    fun providePostRemoteKeysDao(db: CPRDB) = db.postRemoteKeysDao()

}