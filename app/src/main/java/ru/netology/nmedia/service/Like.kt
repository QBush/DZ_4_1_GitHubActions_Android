package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

//заполнен принимаемыми данными
class Like(
    @SerializedName("userID")
    val userID:Long,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("postId")
    val postId: Long,
    @SerializedName("postAuthor")
    val postAuthor: String
) {
}