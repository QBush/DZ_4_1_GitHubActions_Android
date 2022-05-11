package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    //val data get() = repository.data - аналогично строчке выше


    fun onLikeClick(post: Post) = repository.like(post.id)
    fun onShareClick(post: Post) = repository.share(post.id)
    fun onDeleteClick(post: Post) = repository.delete(post.id)
}