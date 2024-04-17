package com.example.composepaginationroom.screens.common.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "post")
data class Post(

    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int? = null,

    @ColumnInfo(name = "title")
    @field:SerializedName("title")
    val title: String? = null,

    @ColumnInfo(name = "body")
    @field:SerializedName("body")
    val body: String? = null,

    @ColumnInfo(name = "userId")
    @field:SerializedName("userId")
    val userId: Int? = null,

    @ColumnInfo(name = "page")
    var page: Int

) : Parcelable
