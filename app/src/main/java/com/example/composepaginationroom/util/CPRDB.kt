package com.example.composepaginationroom.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composepaginationroom.screens.common.model.Post
import com.example.composepaginationroom.screens.posts.model.PostRemoteKeys
import com.example.composepaginationroom.screens.common.db.PostDao
import com.example.composepaginationroom.screens.posts.repository.db.PostRemoteKeysDao

@Database(entities = [Post::class, PostRemoteKeys::class], version = 1)
abstract class CPRDB : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun postRemoteKeysDao(): PostRemoteKeysDao

}