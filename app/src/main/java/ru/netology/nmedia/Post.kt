package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var videoUrl: String? = null,
    var likedByMe: Boolean = false,
    var likeCount: Long = 0,
    var shareCount: Long = 0
){


}