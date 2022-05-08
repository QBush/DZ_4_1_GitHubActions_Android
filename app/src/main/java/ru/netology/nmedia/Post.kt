package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false
){
    val likeCount = MutableLiveData<Int>(10)
    val shareCount = MutableLiveData<Int>(0)
}