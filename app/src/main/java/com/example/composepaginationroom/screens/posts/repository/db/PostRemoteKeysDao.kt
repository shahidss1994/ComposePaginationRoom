package com.example.composepaginationroom.screens.posts.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composepaginationroom.screens.posts.model.PostRemoteKeys

@Dao
interface PostRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PostRemoteKeys>)

    @Query("Select * From post_remote_key Where post_id = :id")
    suspend fun getPostRemoteKeyByPostId(id: Int): PostRemoteKeys?

    @Query("Delete From post_remote_key")
    suspend fun deletePostRemoteKeys()

    @Query("Select created_at From post_remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}