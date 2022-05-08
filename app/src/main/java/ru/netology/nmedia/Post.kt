package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false

) {
    var likeCount = 0
    var shareCount = 0
    var viewCount = 0
}