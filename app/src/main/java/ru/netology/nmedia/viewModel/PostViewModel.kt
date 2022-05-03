package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()


    val data by repository::data
//val data get() = repository.data - аналогично строчке выше

    val likeCount by repository::likeCount
    val shareCount by repository::shareCount

    fun onLikeClick() = repository.like()
    fun onShareClick() = repository.share()

}