package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClick(content: String) {
        if (content.isBlank()) return
        val newPost = Post(
            id = PostRepository.NEW_POST_ID,
            author = "me",
            content = content,
            published = "12.05.2022"
        )
        repository.save(newPost)
        currentPost.value = null
    }


    //region PostInteractionListener

    override fun onLikeClick(post: Post) = repository.like(post.id)
    override fun onShareClick(post: Post) = repository.share(post.id)
    override fun onRemoveClick(post: Post) = repository.delete(post.id)

    // endregion PostInteractionListener
}