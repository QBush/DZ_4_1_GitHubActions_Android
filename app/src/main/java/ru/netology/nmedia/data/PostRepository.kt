package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    val data: LiveData<List<Post>>

    val likeCount: LiveData<Int>
    val shareCount: LiveData<Int>

    fun like(postID : Long)
    fun share()
}