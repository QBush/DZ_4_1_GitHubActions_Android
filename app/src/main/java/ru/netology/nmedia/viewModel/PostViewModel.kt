package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.utils.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String?>()

    val currentPost = MutableLiveData<Post?>(null)

    //region MenuInteractionListener

    fun onSaveButtonClick(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "me",
            content = content,
            published = "12.05.2022"
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onEditClick(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    //endregion MenuInteractionListener

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call() // устанавливаем null значение
    }

    //region PostInteractionListener

    override fun onLikeClick(post: Post) = repository.like(post.id)
    override fun onShareClick(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }
    override fun onRemoveClick(post: Post) = repository.delete(post.id)



    // endregion PostInteractionListener
}