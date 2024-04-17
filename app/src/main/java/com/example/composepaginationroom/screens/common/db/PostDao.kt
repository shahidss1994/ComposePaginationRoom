package com.example.composepaginationroom.screens.common.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composepaginationroom.screens.common.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("Select * From post Order By page, id")
    fun getPosts(): PagingSource<Int, Post>

    @Query("Select * From post where id=:postId")
    fun getPostById(postId: Int): Post

    @Query("Delete From post")
    suspend fun deletePosts()

}