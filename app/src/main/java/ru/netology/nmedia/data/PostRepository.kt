package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    val data: LiveData<Post>

    val likeCount: LiveData<Int>
    val shareCount: LiveData<Int>

    fun like()
    fun share()
}