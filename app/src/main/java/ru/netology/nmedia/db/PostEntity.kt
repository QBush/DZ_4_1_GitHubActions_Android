package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "publishedDate")
    val published: String,
    @ColumnInfo(name = "videoUrl")
    var videoUrl: String?,
    @ColumnInfo(name = "likedByMe")
    var likedByMe: Boolean,
    @ColumnInfo(name = "likeCount")
    var likeCount: Long = 0,
    @ColumnInfo(name = "shareCount")
    var shareCount: Long = 0
) {
}