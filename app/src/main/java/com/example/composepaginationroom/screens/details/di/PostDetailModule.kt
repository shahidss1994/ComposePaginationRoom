package com.example.composepaginationroom.screens.details.di

import com.example.composepaginationroom.util.CPRDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PostDetailModule {

    @Provides
    fun providePostDao(db: CPRDB) = db.postDao()

}