package com.example.composepaginationroom.screens.posts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_remote_key")
data class PostRemoteKeys (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "post_id")
    val postId: Int,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
