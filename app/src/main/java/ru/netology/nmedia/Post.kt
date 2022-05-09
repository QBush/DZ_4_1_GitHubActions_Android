package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likeCount: Long = 10,
    var shareCount : Long = 0
){

}