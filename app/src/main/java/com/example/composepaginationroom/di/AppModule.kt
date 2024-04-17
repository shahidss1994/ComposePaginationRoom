package com.example.composepaginationroom.di

import android.content.Context
import androidx.room.Room
import com.example.composepaginationroom.util.CPRDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CPRDB =
        Room.databaseBuilder(context, CPRDB::class.java, "cprdb")
            .build()

}